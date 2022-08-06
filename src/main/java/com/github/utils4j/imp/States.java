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

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.Collection;

public class States {
  private States() {
  }

  public static <T> T requireNonNull(T o, String message) {
    if (o == null)
      throw new IllegalStateException(message);
    return o;
  }

  public static int requirePositive(int value, String message) {
    if (value <= 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static long requirePositive(long value, String message) {
    if (value <= 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static float requirePositive(float value, String message) {
    if (value <= 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static double requirePositive(double value, String message) {
    if (value <= 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static long requireZeroPositive(long value, String message) {
    if (value < 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static String requireText(Object value, String message) {
    String text;
    if (value == null || !Strings.hasText(text = value.toString()))
      throw new IllegalStateException(message);
    return text;
  }
  
  public static String requireText(String value, String message) {
    if (!Strings.hasText(value))
      throw new IllegalStateException(message);
    return value;
  }

  public static void requireText(String[] roots, String message) {
    requireNonNull(roots, message);
    requirePositive(roots.length, message);
    for (String root : roots)
      requireText(root, message);
  }

  public static int requireZeroPositive(int value, String message) {
    if (value < 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static int requireNegative(int value, String message) {
    if (value >= 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static int requireZeroNegative(int value, String message) {
    if (value > 0)
      throw new IllegalStateException(message);
    return value;
  }

  public static boolean requireTrue(boolean value, String message) {
    if (!value)
      throw new IllegalStateException(message);
    return value;
  }

  public static boolean requireFalse(boolean value, String message) {
    if (value)
      throw new IllegalStateException(message);
    return value;
  }
  
  public static Path requireExists(Path path, String message) {
    return requireExists(path, message, LinkOption.NOFOLLOW_LINKS);
  }
  
  public static Path requireExists(Path path, String message, LinkOption options) {
    if (!Files.exists(requireNonNull(path, message), options))
      throw new IllegalStateException(message);
    return path;
  }
  
  public static <T extends Collection<?>> T requireEmpty(T collection, String message) {
    if (!Containers.isEmpty(collection))
      throw new IllegalStateException(message);
    return collection;
  }

  public static <T extends Collection<?>> T requireNonEmpty(T collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalStateException(message);
    return collection;
  }
  
  public static byte[] requireNonEmpty(byte[] collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalStateException(message);
    return collection;
  }

  public static Certificate[] requireNonEmpty(Certificate[] collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalStateException(message);
    return collection;
  }
  
}
