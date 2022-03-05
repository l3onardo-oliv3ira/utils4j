package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryRun;

import com.github.utils4j.IThreadContext;

public abstract class ThreadContext implements IThreadContext {

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
  public final synchronized void start() {
    stop();
    context = new Thread(name) {
      @Override
      public void run() {
        try {
          beforeRun();
        } catch (Exception e) {
          handleException(e);
          return;
        }
        try {
          doRun();
        } catch(InterruptedException e) {
          interrupt();
        } catch (Exception e) {
          handleException(e);
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
  public final synchronized void stop(long timeout) {
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

  @Override
  public final void stop() {
    stop(0);
  }
  
  protected void beforeRun() throws Exception {}

  protected void afterRun() throws Exception {}
  
  protected void handleException(Throwable e) {
    e.printStackTrace();
  }

  protected abstract void doRun() throws Exception;
}
