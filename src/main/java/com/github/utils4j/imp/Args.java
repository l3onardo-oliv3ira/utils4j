package com.github.utils4j.imp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.Collection;

public class Args {
  private Args() {
  }

  public static <T> T requireNonNull(T o, String message) {
    if (o == null)
      throw new IllegalArgumentException(message);
    return o;
  }

  public static int requirePositive(int value, String message) {
    if (value <= 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static long requirePositive(long value, String message) {
    if (value <= 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static float requirePositive(float value, String message) {
    if (value <= 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static double requirePositive(double value, String message) {
    if (value <= 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static long requireZeroPositive(long value, String message) {
    if (value < 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static String requireText(Object value, String message) {
    String text;
    if (value == null || !Strings.hasText(text = value.toString()))
      throw new IllegalArgumentException(message);
    return text;
  }
  
  public static String requireText(String value, String message) {
    if (!Strings.hasText(value))
      throw new IllegalArgumentException(message);
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
      throw new IllegalArgumentException(message);
    return value;
  }

  public static int requireNegative(int value, String message) {
    if (value >= 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static int requireZeroNegative(int value, String message) {
    if (value > 0)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static boolean requireTrue(boolean value, String message) {
    if (!value)
      throw new IllegalArgumentException(message);
    return value;
  }

  public static Path requireExists(Path path, String message) {
    return requireExists(path, message, LinkOption.NOFOLLOW_LINKS);
  }

  public static Path requireFolderExists(Path path, String message) {
    if (!requireExists(path, message, LinkOption.NOFOLLOW_LINKS).toFile().isDirectory())
      throw new IllegalArgumentException(message);
    return path;
  }

  public static Path requireExists(Path path, String message, LinkOption options) {
    if (!Files.exists(Args.requireNonNull(path, message), options))
      throw new IllegalArgumentException(message);
    return path;
  }
  
  public static <T extends Collection<?>> T requireEmpty(T collection, String message) {
    if (!Containers.isEmpty(collection))
      throw new IllegalArgumentException(message);
    return collection;
  }

  public static <T extends Collection<?>> T requireNonEmpty(T collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalArgumentException(message);
    return collection;
  }
  
  public static <T> T[] requireNonEmpty(T[] collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalArgumentException(message);
    return collection;
  }
  
  
  public static byte[] requireNonEmpty(byte[] collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalArgumentException(message);
    return collection;
  }

  public static Certificate[] requireNonEmpty(Certificate[] collection, String message) {
    if (Containers.isEmpty(collection))
      throw new IllegalArgumentException(message);
    return collection;
  }

  public static File requireExists(File input, String message) {
    if (input == null || !input.exists())
      throw new IllegalArgumentException(message);
    return input;
  }

  public static long requireLong(String value, String message) {
    if (!Strings.isLong(value))
      throw new IllegalArgumentException(message);
    return Long.valueOf(value);
  }
  
  public static int requireInt(String value, String message) {
    if (!Strings.isInt(value))
      throw new IllegalArgumentException(message);
    return Integer.valueOf(value);
  }
  
  public static float requireFloat(String value, String message) {
    if (!Strings.isFloat(value))
      throw new IllegalArgumentException(message);
    return Float.valueOf(value);
  }

  public static double requireDouble(String value, String message) {
    if (!Strings.isDouble(value))
      throw new IllegalArgumentException(message);
    return Double.valueOf(value);
  }

  public static boolean requireBoolean(String value, String message) {
    if (!Strings.isBoolean(value))
      throw new IllegalArgumentException(message);
    return Boolean.valueOf(value);
  }
}
