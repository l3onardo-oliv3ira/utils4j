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

import static com.github.utils4j.imp.Throwables.runQuietly;

import java.util.concurrent.atomic.AtomicReference;

import com.github.utils4j.ILifeCycle;

public abstract class ThreadContext<E extends Exception> implements ILifeCycle<E> {

  private final AtomicReference<Thread> context = new AtomicReference<>();
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
    return context.get() != null;
  }
  
  protected void checkIsAlive() {
    States.requireTrue(isStarted(), "context not available");
  }
  
  @Override
  public final synchronized void start() throws E {
    stop();
    context.set(new Thread(name) {
      @Override
      public void run() {
        try {
          beforeRun();
        } catch (Exception e) {
          context.set(null);
          handleException(e);
          return;
        }
        try {
          doRun();
        } catch(InterruptedException e) {
          interrupt();
        } catch (Exception e) {
          runQuietly(() -> ThreadContext.this.handleException(e));
        } finally {
          runQuietly(ThreadContext.this::afterRun);
          context.set(null);
        }
      }
    });
    context.get().setDaemon(deamon);
    context.get().start();
  }
  
  @Override
  public final void stop() throws E {
    stop(0);
  }

  @Override
  public final synchronized void stop(long timeout) throws E {
    Thread thread; 
    if ((thread = context.getAndSet(null)) != null) {
      thread.interrupt();
      try {
        thread.join(timeout);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
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
