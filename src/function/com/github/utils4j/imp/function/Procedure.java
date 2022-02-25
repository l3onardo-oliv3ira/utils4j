package com.github.utils4j.imp.function;

@FunctionalInterface
public interface Procedure<R, E extends Exception> {
  R call() throws E;
}
