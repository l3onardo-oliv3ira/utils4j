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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class SortedStoredProperties extends Properties {

  public void storeSorted(OutputStream out, String comments) throws IOException {
    Properties sortedProps = new Properties() {
      @Override
      public Set<Map.Entry<Object, Object>> entrySet() {
        Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<Map.Entry<Object, Object>>(new Comparator<Map.Entry<Object, Object>>() {
          @Override
          public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
            return o1.getKey().toString().compareTo(o2.getKey().toString());
          }
        });
        sortedSet.addAll(super.entrySet());
        return sortedSet;
      }

      @Override
      public Set<Object> keySet() {
        return new TreeSet<Object>(super.keySet());
      }

      @Override
      public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
      }

    };
    sortedProps.putAll(this);
    sortedProps.store(out, comments);
  }
}
