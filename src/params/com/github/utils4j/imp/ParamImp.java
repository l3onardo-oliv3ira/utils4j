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

import static com.github.utils4j.imp.Args.requireText;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.utils4j.IParam;

class ParamImp implements IParam {

  static final IParam NULL = new ParamImp(null);
  
  static ParamImp of(String name, Object value) {
    return new ParamImp(name, value);
  }
  
  private final String name;
  
  private final Optional<Object> value;
  
  private ParamImp(Object value) {
    this.name = null;
    this.value = Optional.ofNullable(value);
  }

  private ParamImp(String name, Object value) {
    this.name = requireText(name, "name can't be null").trim();
    this.value = Optional.ofNullable(value);
  }
  
  @Override
  public final String getName() {
    return name;
  }
  
  @Override
  public final <T> T get() {
    return orElse(null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T orElse(T defaultIf) {
    try {
      return (T)value.orElse(defaultIf);
    } catch(Throwable e) {
      return defaultIf;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <X extends Throwable, T> T orElseThrow(Supplier<? extends X> supplier) throws X {
    try {
      return (T)value.orElseThrow(supplier);
    } catch(Throwable e) {
      Throwable t = supplier.get();
      t.addSuppressed(e);
      throw (X)t;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T orElseGet(Supplier<? extends T> other) {
    try {
      return (T)value.orElseGet(other);
    }catch(Throwable e) {
      return other.get();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void ifPresent(Consumer<T> consumer) {
    value.ifPresent((Consumer<Object>)consumer);      
  }

  @Override
  public boolean isPresent() {
    return value.isPresent();
  }
}
