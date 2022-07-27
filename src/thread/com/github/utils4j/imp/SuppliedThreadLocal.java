package com.github.utils4j.imp;

import java.util.function.Supplier;

public final class SuppliedThreadLocal<T> extends ThreadLocal<T> {

  private final Supplier<? extends T> supplier;

  public SuppliedThreadLocal(Supplier<? extends T> supplier) {
      this.supplier = Args.requireNonNull(supplier, "supplier is null");
  }

  @Override
  protected T initialValue() {
      return supplier.get();
  }
}