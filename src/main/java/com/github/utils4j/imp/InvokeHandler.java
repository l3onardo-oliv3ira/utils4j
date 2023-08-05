/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


package com.github.utils4j.imp;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.utils4j.imp.function.IProvider;

public abstract class InvokeHandler<E extends Throwable> {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(InvokeHandler.class);
  
  private static final Runnable DEFAULT_FINALLY_BLOCK = () -> {};

  private static final Consumer<Throwable> DEFAULT_CATCH_BLOCK = (e) -> LOGGER.warn("InvokeHandler (catchBlock)", e); 
  
  protected InvokeHandler() {}
  
  public final <T> T invoke(IProvider<T> tryBlock) throws E { 
    return invoke(tryBlock, DEFAULT_CATCH_BLOCK);
  }

  public final <T> T invoke(IProvider<T> tryBlock, Runnable finallyBlock) throws E {
    return invoke(tryBlock, DEFAULT_CATCH_BLOCK, finallyBlock);
  }
    
  public final <T> T invoke(IProvider<T> tryBlock, Consumer<Throwable> catchBlock) throws E {
    return invoke(tryBlock, catchBlock, DEFAULT_FINALLY_BLOCK);
  }

  public abstract <T> T invoke(IProvider<T> tryBlock, Consumer<Throwable> catchBlock, Runnable finallyBlock) throws E;
}
