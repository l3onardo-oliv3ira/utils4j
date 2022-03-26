/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


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
