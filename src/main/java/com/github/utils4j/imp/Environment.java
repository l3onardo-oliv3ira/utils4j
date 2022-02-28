package com.github.utils4j.imp;

import static java.lang.System.getProperty;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

public class Environment {
  
  private Environment() {}
  
  public static Optional<Path> pathFrom(final String key) {
    return pathFrom(key, () -> null);
  }
  
  public static Optional<Path> pathFrom(String key, String currentOrHome) {
    return pathFrom(key, () -> currentOrHome);
  }
  
  public static Optional<Path> pathFrom(final String key, Supplier<String> defaultIfNothing) {
    if (key == null)
      return Optional.empty();
    String value = System.getenv(key);
    if (value == null) {
      value = System.getProperty(key);
    }
    if (value == null) {
      value = System.getProperty(key.toLowerCase());
    }
    if (value == null) {
      value = System.getProperty(key.toUpperCase());
    }
    if (value == null) {
      value = System.getenv(key.toLowerCase());
    }
    if (value == null) {
      value = System.getenv(key.toUpperCase());
    }
    if (value == null && defaultIfNothing != null) {
      value = defaultIfNothing.get();
    }

    int time = 1;
    do {
      if (value != null) {
        final File file = new File(value);
        if (file.exists()) {
          return Optional.of(file.toPath().toAbsolutePath());
        }
      }
      
      if (time++ >= 2 || defaultIfNothing == null) {
        break;
      }
      
      value = getProperty("user.home") + getProperty("file.separator") + Strings.trim(defaultIfNothing.get());
    }while(true);
      
    return Optional.empty();
  }
}
