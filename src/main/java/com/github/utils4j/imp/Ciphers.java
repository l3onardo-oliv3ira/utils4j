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

import java.security.Key;

import javax.crypto.Cipher;

public final class Ciphers {
  
  private Ciphers() {}
  
  public static final String ALGORITHM_RSA = "RSA/ECB/PKCS1Padding";

  public static byte[] encryptWithRsa(final byte[] message, final Key key) throws Exception {
    return encrypt(message, key, ALGORITHM_RSA);
  }

  public static byte[] decryptWithRsa(final byte[] message, final Key key) throws Exception {
    return decrypt(message, key, ALGORITHM_RSA);
  }

  public static byte[] encrypt(final byte[] message, final Key key, final String algorithm) throws Exception {
    final Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(1, key);
    return cipher.doFinal(message);
  }

  public static byte[] decrypt(final byte[] message, final Key key, final String algorithm) throws Exception {
    final Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(2, key);
    return cipher.doFinal(message);
  }
}
