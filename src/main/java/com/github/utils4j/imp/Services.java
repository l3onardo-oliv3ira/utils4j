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
