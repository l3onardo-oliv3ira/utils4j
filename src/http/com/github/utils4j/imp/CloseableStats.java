package com.github.utils4j.imp;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.core5.pool.ConnPoolStats;
import org.apache.hc.core5.pool.PoolStats;

public interface CloseableStats extends ConnPoolStats<HttpRoute>, AutoCloseable {
  CloseableStats IDLE = new CloseableStats() {
    private final PoolStats EMPTY = new PoolStats(-1, -1, -1, -1);
    
    @Override
    public PoolStats getTotalStats() {
      return EMPTY;
    }
    
    @Override
    public PoolStats getStats(HttpRoute route) {
      return EMPTY;
    }
    
    @Override
    public void close() throws Exception {
      
    }
  };
}
