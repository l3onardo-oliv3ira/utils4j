package com.github.utils4j.imp.function;

@FunctionalInterface
public interface Runnable<T, E extends Exception> {
  void run(T T) throws E;
}
