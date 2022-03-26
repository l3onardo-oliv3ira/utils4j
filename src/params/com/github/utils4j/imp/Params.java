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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.utils4j.IParam;
import com.github.utils4j.IParams;

public class Params implements IParams {
  
  public static final Params EMPTY = new Params() {
    @Override
    public Params of(String name, Object value) {
      throw new IllegalStateException("Unabled to create param on EMPTY instance");
    }
  };
  
  public static Params create() {
    return new Params();
  }
  
  private static Params create(Map<String, IParam> map) {
    return new Params(new HashMap<String, IParam>(map));
  }
  
  private final Map<String, IParam> params;
  
  protected Params() {
    this(new HashMap<>());
  }
  
  private Params(HashMap<String, IParam> params) {
    this.params = params;
  }

  public Params of(String name, Optional<?> value) {
    params.put(name, ParamImp.of(name, value.orElse(null)));
    return this;
  }
  
  public Params of(String name, Object value) {
    params.put(name, ParamImp.of(name, value));
    return this;
  }

  public Params clone() {
    return Params.create(new HashMap<String, IParam>(this.params));
  }
  
  @Override
  public final IParam get(String key) {
    return Optional.ofNullable(params.get(key)).orElse(ParamImp.NULL);
  }
}
