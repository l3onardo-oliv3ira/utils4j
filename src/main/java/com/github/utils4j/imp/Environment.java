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

import static java.lang.System.getProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

public final class Environment {
  
  private Environment() {}
  
  public static Optional<String> valueFrom(final String environmentVariableKey) {
    return valueFrom(environmentVariableKey, (String)null);
  }
  
  public static Optional<String> valueFrom(final String environmentVariableKey, String defauValueIfEmpty) {
    return valueFrom(environmentVariableKey, () -> defauValueIfEmpty);
  }

  public static Optional<Path> pathFrom(final String environmentVariableKey) { 
    return pathFrom(environmentVariableKey, false);
  }

  public static Optional<Path> pathFrom(final String environmentVariableKey, boolean defaultToUserHome) { 
    return pathFrom(environmentVariableKey, defaultToUserHome, false);
  }

  public static Optional<Path> pathFrom(final String environmentVariableKey, boolean defaultToUserHome, boolean mustExists) { 
    return pathFrom(environmentVariableKey, defaultToUserHome ? Paths.get(getProperty("user.home")) : null, mustExists);
  }

  public static Optional<Path> pathFrom(final String environmentVariableKey, Path defaultIfNothing) {
    return pathFrom(environmentVariableKey, defaultIfNothing, false);
  }

  public static Optional<Path> pathFrom(final String environmentVariableKey, Path defaultIfNothing, boolean mustExists) { 
    return pathFrom(environmentVariableKey, () -> defaultIfNothing, mustExists);
  }
  
  public static Optional<Path> pathFrom(final String environmentKey, Supplier<Path> defaultIfNothing) { 
    return pathFrom(environmentKey, defaultIfNothing, false);
  }
  
  public static Optional<Path> resolveTo(final String environmentVariableKey, String fileName) {
    return resolveTo(environmentVariableKey, fileName, false);
  }
  
  public static Optional<Path> resolveTo(final String environmentVariableKey, String fileName, boolean defaultToUserHome) {
    return resolveTo(environmentVariableKey, fileName, defaultToUserHome, false);
  }
  
  public static Optional<Path> resolveTo(final String environmentVariableKey, String fileName, boolean defaultToUserHome, boolean mustExists) {
    Optional<Path> basePath = pathFrom(environmentVariableKey, defaultToUserHome, mustExists);
    if (!basePath.isPresent())
      return Optional.empty();
    Path resolvedPath = basePath.get().resolve(fileName);
    if (mustExists && !resolvedPath.toFile().exists())
      return Optional.empty();
    return Optional.of(resolvedPath);
  }
    
  public static Optional<Path> pathFrom(final String environmentKey, Supplier<Path> defaultIfNothing, boolean mustExists) {
    Optional<String> environmentKeyPath = valueFrom(environmentKey);
    Path basePath = null;
    if (environmentKeyPath.isPresent()) {
      basePath = Paths.get(environmentKeyPath.get());
    } 
    
    if (basePath != null) {
      if (mustExists) {
        if (basePath.toFile().exists())
          return Optional.of(basePath);
        basePath = null;
      }
    }
    
    if (basePath == null && defaultIfNothing != null) {
      basePath = defaultIfNothing.get();
    }
    
    if (basePath == null || (mustExists && !basePath.toFile().exists())) {
      return Optional.empty();
    }

    return Optional.of(basePath);
  }
  
  public static Optional<String> valueFrom(final String environmentVariableKey, Supplier<String> defauValueIfEmpty) {
    if (environmentVariableKey == null) {
      if (defauValueIfEmpty == null)
        return Optional.empty();
      return Optional.ofNullable(defauValueIfEmpty.get());
    }
    String value = System.getProperty(environmentVariableKey);
    if (value == null) {
      value = System.getProperty(environmentVariableKey.toLowerCase());
    }
    if (value == null) {
      value = System.getProperty(environmentVariableKey.toUpperCase());
    }
    if (value == null) {
      value = System.getenv(environmentVariableKey);
    }
    if (value == null) {
      value = System.getenv(environmentVariableKey.toLowerCase());
    }
    if (value == null) {
      value = System.getenv(environmentVariableKey.toUpperCase());
    }
    if (value == null && defauValueIfEmpty != null) {
      value = defauValueIfEmpty.get();
    }
    return Optional.ofNullable(value);
  }
}
