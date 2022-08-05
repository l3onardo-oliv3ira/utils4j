package com.github.utils4j.imp;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.pool.PoolStats;

public final class PoolingStats implements CloseableStats {

  private PoolingHttpClientConnectionManager pool;
  
  public PoolingStats(PoolingHttpClientConnectionManager pool) {
    this.pool = Args.requireNonNull(pool, "pool is null");
  }
  
  @Override
  public PoolStats getTotalStats() {
    return pool.getTotalStats();
  }

  @Override
  public PoolStats getStats(HttpRoute route) {
    return pool.getStats(route);
  }

  @Override
  public void close() throws Exception {
    pool.close();
  }
}
