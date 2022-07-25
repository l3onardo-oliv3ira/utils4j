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

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.utils4j.imp.function.Predicates;

public class Containers {
  private Containers() {
  }

  public static boolean isEmpty(Collection<?> c) {
    return c == null || c.size() == 0;
  }

  public static boolean isEmpty(Map<?, ?> m) {
    return m == null || m.isEmpty();
  }

  public static boolean isEmpty(Set<?> set) {
    return set == null || set.isEmpty();
  }

  public static <T> List<T> toList(Set<T> set) {
    return isEmpty(set) ? emptyList() : new ArrayList<>(set);
  }
  
  public static <T> List<T> toUnmodifiableList(Set<T> set) {
    return isEmpty(set) ? emptyList() : unmodifiableList(new ArrayList<>(set));
  }

  public static String firstText(Collection<String> values) {
    if (values == null || values.isEmpty())
      return "";
    return Strings.text(values.iterator().next());
  }

  public static boolean isEmpty(Enumeration<?> e) {
    return e == null || !e.hasMoreElements();
  }

  public static boolean isEmpty(Object[] value) {
    return value == null || value.length == 0;    
  }

  public static boolean isEmpty(Number[] value) {
    return value == null || value.length == 0;    
  }

  public static boolean isEmpty(byte[] value) {
    return value == null || value.length == 0;    
  }
  
  @SafeVarargs
  public static <T> List<T> arrayList(T... array) {
    return arrayList(Predicates.all(), array);
  }
  
  @SafeVarargs
  public static <T> List<T> arrayList(Predicate<T> filter, T... array) {
    return Arrays.asList(array).stream().filter(filter).collect(Collectors.toList());
  }

  public static String[] arrayOf(List<String> container) {
    if (container == null)
      return null;
    return container.toArray(new String[container.size()]);
  }
}
