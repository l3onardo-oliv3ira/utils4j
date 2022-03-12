package com.github.utils4j.imp;

import java.util.NoSuchElementException;

import com.github.utils4j.IResetableIterator;

public class ArrayIterator<T> implements IResetableIterator<T> {
  private int current = 0;
  private T[] array;
  
  public ArrayIterator(T[] array) {
    this.array = Args.requireNonNull(array, "array is null");
  }
  
  @Override
  public final boolean hasNext() {
    return current < array.length;
  }

  @Override
  public final T next() {
    if (current == array.length)
      throw new NoSuchElementException();
    return array[current++];
  }

  @Override
  public final void reset() {
    current = 0;
  }
}
