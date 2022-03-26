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
