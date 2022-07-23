package com.github.utils4j;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IAcumulator<T> extends Consumer<T>, Supplier<T> {
  T handleFail(IOException e);
}