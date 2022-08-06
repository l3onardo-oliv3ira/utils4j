package com.github.utils4j.imp;

import java.util.function.Supplier;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.pool.PoolStats;

public final class PoolingStats implements Supplier<PoolStats> {

  private final PoolingHttpClientConnectionManager pool;
  
  public PoolingStats(PoolingHttpClientConnectionManager pool) {
    this.pool = Args.requireNonNull(pool, "pool is null");
  }
  
  @Override
  public PoolStats get() {
    return pool.getTotalStats();
  }
}
