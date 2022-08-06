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

public class Threads {
  private Threads(){}
  
  public static void sleep(long time) {
    if (time > 0)
      try {
        safeSleep(time);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
  }
  
  /**
   * Por alguma razao o metodo sleep da classe Thread retorna sem que 
   * seja passado efetivamente pelo periodo de tempo informado no parametro.
   * Este método garante que pelo menos 'millis' milisegundos tenham se 
   * passado antes do retorno, ou seja, pode se passar alguns milisegundos
   * a mais, mas nunca a menos. 
   * @param millis
   * @throws InterruptedException
   */
  public static void safeSleep(long millis) throws InterruptedException {
    if (millis == 0)
      return;
    long done = 0;
    do {
      long before = System.currentTimeMillis();
      Thread.sleep(millis - done);
      long after = System.currentTimeMillis();
      done += (after - before);
    } while (done < millis);
  }

  public static Thread startAsync(Runnable runnable) {
    return startAsync(runnable, 0);
  }
  
  public static Thread startAsync(Runnable runnable, long delay) {
    return startAsync(Strings.empty(), runnable, delay);
  }
  
  public static Thread startAsync(String threadName, Runnable runnable) { 
    return startAsync(threadName, runnable, 0);
  }

  public static Thread startAsync(String threadName, Runnable runnable, long delay) {
    Args.requireNonNull(threadName, "threadName is empty");
    Args.requireNonNull(runnable, "runnable is null");
    Args.requireZeroPositive(delay, "delay is negative");
    Thread t = new Thread(() -> {
      Threads.sleep(delay);
      runnable.run();
    }, threadName + " from parent: " + Thread.currentThread().getName());
    t.start();
    return t;
  }
  
  public static ShutdownHookThread shutdownHook(Runnable runnable) {
    return shutdownHookAdd(runnable, "shutdownhook:" + Dates.stringNow());
  }
  
  public static ShutdownHookThread shutdownHookAdd(Runnable runnable, String name) {
    Args.requireText(name, "threadName is empty");
    Args.requireNonNull(runnable, "runnable is null");
    ShutdownHookThread t = new ShutdownHookThread(name, runnable);
    Runtime.getRuntime().addShutdownHook(t);
    return t;
  }

  public static void shutdownHookRem(ShutdownHookThread jvmHook) {
    if (!isShutdownHook()) {
      runQuietly(() -> Runtime.getRuntime().removeShutdownHook(jvmHook), true);
    }
  }
  
  public static class ShutdownHookThread extends Thread {
    ShutdownHookThread(String name, Runnable r){
      super(r, name);
    }
  }

  public static boolean isShutdownHook() {
    return Thread.currentThread() instanceof ShutdownHookThread;
  }
}
