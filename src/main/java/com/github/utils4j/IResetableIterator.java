package com.github.utils4j;

import java.util.Iterator;

public interface IResetableIterator<T> extends Iterator<T>{
  public void reset();
}
