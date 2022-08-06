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

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.utils4j.IKeyValue;

public final class Pair<K, V> implements IKeyValue<K,V>{

  private final K key;
  
  private final V value;
  
  public static <K, V> Pair<K, V> of(K key, V value) {
    return new Pair<K, V>(key, value);
  }
  
  public static <K, V> Pair<K, V> of(K k, Supplier<V> v) {
    Args.requireNonNull(v, "key supplier is null");
    return of(k, v.get());
  }

  public static <K, V> Pair<K, V> of(Supplier<K> k, V v) {
    Args.requireNonNull(k, "key supplier is null");
    return of(k.get(), v);
  }

  public static <K, V> Pair<K, V> of(Supplier<K> k, Supplier<V> v) {
    Args.requireNonNull(k, "key supplier is null");
    Args.requireNonNull(v, "value supplier is null");
    return of(k.get(), v.get());
  }

  private Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }
  
  public Stream<V> valueStream() {
    return Stream.of(getValue());
  }
  
  public Stream<K> keyStream() {
    return Stream.of(getKey());
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }
}
