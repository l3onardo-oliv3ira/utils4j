package com.github.utils4j.imp;

public final class Retryable<T> {
  
  public static <T> T attempt(long interval, long timeout, Executable<T> exec) throws RetryTimeoutException, Exception{
    return attempt(0L, interval, timeout, exec);
  }
  
  public static <T> T attempt(long delay, long interval, long timeout, Executable<T> exec) throws RetryTimeoutException, Exception{
    return new Retryable<T>(exec).execute(delay, interval, timeout);
  }

  @FunctionalInterface
  public static interface Executable<T> {
    T execute() throws TemporaryException, Exception;
  }
  
  private final Executable<T> executable;
  
  private Retryable(Executable<T> executable) {
    this.executable = executable;
  }

  private T execute(long delay, long interval, long timeout) throws RetryTimeoutException, Exception {
    long start = System.currentTimeMillis();
    if (delay > 0L)
      Threads.sleep(delay);
    do {
      try {
        return executable.execute();
      } catch (TemporaryException e) {
        if (System.currentTimeMillis() - start < timeout) {
          Threads.sleep(interval);
          continue;
        }
        throw new RetryTimeoutException(e);
      }
    } while(true);
  }
}