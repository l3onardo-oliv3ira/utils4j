package com.github.utils4j.imp;

import java.util.function.Consumer;

import com.github.utils4j.imp.function.Supplier;

public abstract class InvokeHandler<E extends Throwable> {
  
  protected InvokeHandler() {}
  
  public final <T> T invoke(Supplier<T> tryBlock) throws E { 
    return invoke(tryBlock, (e) -> {});
  }

  public final <T> T invoke(Supplier<T> tryBlock, Runnable finallyBlock) throws E {
    return invoke(tryBlock, (e) -> {}, finallyBlock);
  }
    
  public final <T> T invoke(Supplier<T> tryBlock, Consumer<Throwable> catchBlock) throws E {
    return invoke(tryBlock, catchBlock, () -> {});
  }

  public abstract <T> T invoke(Supplier<T> tryBlock, Consumer<Throwable> catchBlock, Runnable finallyBlock) throws E;
}
