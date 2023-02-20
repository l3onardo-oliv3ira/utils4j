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

import java.util.Locale;

public enum Sizes {
  B(1, "B"),
  KB(B.size * 1024, "KB"),
  MB(KB.size * 1024, "MB"),
  GB(MB.size * 1024, "GB"),
  TB(GB.size * 1024, "TB"),
  PB(TB.size * 1024, "PB");
  
  private final long size;
  private final String label;
  
  private Sizes(long size, String label) {
    this.size = size;
    this.label = label;
  }
  
  public static String defaultFormat(double size) {
    if (size < KB.size)
      return B.format(size);
    if (size < MB.size)
      return KB.format(size);
    if (size < GB.size)
      return MB.format(size);
    if (size < TB.size)
      return GB.format(size);
    return TB.format(size);
  }
  
  public long toBytes(double size) {
    return (long)(this.size * size); 
  }

  private String format(double size) {
    double value = size / this.size;
    if (size % this.size == 0)
      return String.format("%d %s", (long)value, label);
    return String.format(Locale.US, "%.2f %s", value, label);
  }
}
