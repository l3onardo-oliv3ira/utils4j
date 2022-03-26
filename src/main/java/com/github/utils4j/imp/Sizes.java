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

public enum Sizes {
  _1B(1, "B"),
  _1KB(_1B.size * 1024, "KB"),
  _1MB(_1KB.size * 1024, "MB"),
  _1GB(_1MB.size * 1024, "GB"),
  _1TB(_1GB.size * 1024, "TB"),
  _1PB(_1TB.size * 1024, "PB");
  
  private final long size;
  private final String label;
  
  private Sizes(long size, String label) {
    this.size = size;
    this.label = label;
  }
  
  public static String defaultFormat(long size) {
    if (size < _1KB.size)
      return _1B.format(size);
    if (size < _1MB.size)
      return _1KB.format(size);
    if (size < _1GB.size)
      return _1MB.format(size);
    if (size < _1TB.size)
      return _1GB.format(size);
    return _1TB.format(size);
  }

  String format(long size) {
    return String.format("%.2f %s", ((double)size / this.size), label);
  }
}
