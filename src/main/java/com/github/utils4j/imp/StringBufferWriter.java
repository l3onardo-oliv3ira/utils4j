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

import java.io.Serializable;
import java.io.Writer;

public class StringBufferWriter extends Writer implements Serializable {

  private final StringBuffer buffer;

  public StringBufferWriter() {
    this.buffer = new StringBuffer();
  }

  public StringBufferWriter(int capacity) {
    this.buffer = new StringBuffer(capacity);
  }

  public StringBufferWriter(StringBuffer buffer) {
    this.buffer = buffer != null ? buffer : new StringBuffer();
  }

  @Override
  public Writer append(char value) {
    buffer.append(value);
    return this;
  }

  @Override
  public Writer append(CharSequence value) {
    buffer.append(value);
    return this;
  }

  @Override
  public Writer append(CharSequence value, int start, int end) {
    buffer.append(value, start, end);
    return this;
  }

  @Override
  public void close() {
  }

  @Override
  public void flush() {
  }

  @Override
  public void write(String value) {
    if (value != null) {
      buffer.append(value);
    }
  }

  @Override
  public void write(char[] value, int offset, int length) {
    if (value != null) {
      buffer.append(value, offset, length);
    }
  }

  public StringBuffer getBuilder() {
    return buffer;
  }

  @Override
  public String toString() {
    return buffer.toString();
  }
}
