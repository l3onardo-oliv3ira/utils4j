package com.github.utils4j.imp.function;

@FunctionalInterface
public interface Executable<T extends Exception> {
  void execute() throws T;
}

