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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.github.utils4j.InputStreamFactory;

public class OpenByteArrayOutputStream extends ByteArrayOutputStream implements InputStreamFactory {
  
  public static interface IBufferProcessor<T, E extends Throwable> {
    T process(byte[] buffer, int offset, int length) throws E;
  }
  
  public OpenByteArrayOutputStream() {
  }

  public OpenByteArrayOutputStream(long size) {
    super(size > Integer.MAX_VALUE ? 32 : (int)size);
  }

  public final synchronized String asString() {
    return new String(super.buf, 0, super.size());
  }

  public final synchronized String asString(String charset) {
    return asString(Charset.forName(charset));
  }

  public final synchronized String asString(Charset charset) {
    return new String(super.buf, 0, super.size(), charset);
  }
  
  @Override
  public final synchronized InputStream toInputStream() { //don't invoke
    return new ByteArrayInputStream(super.buf, 0, super.size());
  }
  
  public final synchronized <T, E extends Throwable> T process(IBufferProcessor<T, E> processor) throws E {
    return processor.process(super.buf, 0, super.size());
  }
  
  @Override
  public final synchronized void close() {
    this.buf = new byte[32];
    this.count = 0;
  }
}
