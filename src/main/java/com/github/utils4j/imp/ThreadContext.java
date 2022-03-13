package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryRun;

import com.github.utils4j.ILifeCycle;

public abstract class ThreadContext<E extends Exception> implements ILifeCycle<E> {

  private volatile Thread context;
  private final String name;
  private final boolean deamon;

  public ThreadContext(String name) {
    this(name, false);
  }

  public ThreadContext(String name, boolean deamon) {
    this.deamon = deamon;
    this.name = name;
  }

  @Override
  public final boolean isStarted() {
    return context != null;
  }
  
  protected void checkIsAlive() {
    States.requireTrue(context != null, "context not available");
  }
  
  @Override
  public final synchronized void start() throws E {
    stop();
    context = new Thread(name) {
      @Override
      public void run() {
        try {
          beforeRun();
        } catch (Exception e) {
          context = null;
          handleException(e);
          return;
        }
        try {
          doRun();
        } catch(InterruptedException e) {
          interrupt();
        } catch (Exception e) {
          tryRun(() -> ThreadContext.this.handleException(e));
        } finally {
          tryRun(ThreadContext.this::afterRun);
          context = null;
        }
      }
    };
    context.setDaemon(deamon);
    context.start();
  }
  
  @Override
  public final void stop() throws E {
    stop(0);
  }

  @Override
  public final synchronized void stop(long timeout) throws E {
    if (context != null) {
      context.interrupt();
      try {
        context.join(timeout);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        context = null;
      }
    }
  }

  protected void beforeRun() throws Exception {}

  protected void afterRun() throws Exception {}
  
  protected void handleException(Throwable e) {
    e.printStackTrace();
  }

  protected abstract void doRun() throws Exception;
}
