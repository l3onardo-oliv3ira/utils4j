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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Services {
  private static final Logger LOGGER = LoggerFactory.getLogger(Services.class);
  
  private Services() {
  }

  public static void shutdownNow(ExecutorService pool) {
    shutdownNow(pool, 5);
  }

  public static void shutdownNow(ExecutorService pool, long timeoutSec) {
    if (pool != null) {
      LOGGER.debug("Impedindo que novas tarefas sejam submetidas");
      pool.shutdown();
      LOGGER.debug("Cancelando tarefas atualmente em execução");
      pool.shutdownNow();
      waitFor(pool, timeoutSec);
    }
  }

  public static void shutdown(ExecutorService pool) {
    if (pool != null) {
      pool.shutdown();
      waitFor(pool, 60);
    }
  }

  private static void waitFor(ExecutorService pool, long timeout) {
    boolean shutdown = false;
    do{
      try {
        LOGGER.debug("Aguardando enquanto houver tarefas sendo finalizadas por " + timeout + " segundos");
        shutdown = pool.awaitTermination(timeout, TimeUnit.SECONDS);
      } catch (InterruptedException ie) {
        LOGGER.debug("Capturada InterruptedException");
        Thread.currentThread().interrupt();
      }
      if (!shutdown) {
        LOGGER.debug("Novo pedido de cancelamento de tarefas em execução");
        pool.shutdownNow();
      }
    }while (!shutdown);
  }
}
