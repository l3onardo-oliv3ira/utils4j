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

import java.math.BigInteger;

public class ArrayTools {
  private ArrayTools() {}
  
  public static byte[] convert(byte[] input, int offset, int len) {
    if ((offset == 0) && (len == input.length)) {
      return input;
    } else {
      byte[] t = new byte[len];
      System.arraycopy(input, offset, t, 0, len);
      return t;
    }
  }

  public static byte[] subarray(byte[] b, int ofs, int len) {
    byte[] out = new byte[len];
    System.arraycopy(b, ofs, out, 0, len);
    return out;
  }

  public static byte[] concat(byte[] b1, byte[] b2) {
    byte[] b = new byte[b1.length + b2.length];
    System.arraycopy(b1, 0, b, 0, b1.length);
    System.arraycopy(b2, 0, b, b1.length, b2.length);
    return b;
  }

  public static long[] concat(long[] b1, long[] b2) {
    if (b1.length == 0) {
      return b2;
    }
    long[] b = new long[b1.length + b2.length];
    System.arraycopy(b1, 0, b, 0, b1.length);
    System.arraycopy(b2, 0, b, b1.length, b2.length);
    return b;
  }

  public static byte[] getMagnitude(BigInteger bi) {
    byte[] b = bi.toByteArray();
    if ((b.length > 1) && (b[0] == 0)) {
      int n = b.length - 1;
      byte[] newarray = new byte[n];
      System.arraycopy(b, 1, newarray, 0, n);
      b = newarray;
    }
    return b;
  }

  public static byte[] getBytesUTF8(String s) {
    try {
      return s.getBytes("UTF8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
