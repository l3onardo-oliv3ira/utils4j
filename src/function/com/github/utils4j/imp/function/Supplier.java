package com.github.utils4j.imp.function;

@FunctionalInterface
public interface Supplier<T> {
  T get() throws Exception;
}

