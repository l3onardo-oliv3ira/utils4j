package com.github.utils4j;

import java.util.Iterator;

public interface ResetableIterator<T> extends Iterator<T>{
  public void reset();
}
