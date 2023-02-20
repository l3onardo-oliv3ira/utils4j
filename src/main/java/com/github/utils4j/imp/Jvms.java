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

import static com.github.utils4j.imp.Directory.stringPath;
import static com.github.utils4j.imp.Strings.trim;
import static java.lang.String.format;

import java.io.File;
import java.nio.file.Path;

//$MAJOR.$MINOR.$SECURITY.$PATCH for java 9+
//1.$MAJOR.$MINOR_$UPDATE-$BUILD


public class Jvms {
  private Jvms() {}

  public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  public static final String SYSTEM_ROOT = computeSystemRoot();

  public static final String JAVA_VERSION = computeJavaVersion();

  public static boolean isWindows() {
    return (OS_NAME.indexOf("win") >= 0);
  }
  
  public static boolean isMac() {
    return (OS_NAME.indexOf("mac") >= 0);
  }
  
  public static boolean is64Bits() {
    return System.getProperty("os.arch", "").contains("64");
  }
  
  public static boolean isUnix() {
    return (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0);
  }
  
  public static boolean isSolaris() {
    return (OS_NAME.indexOf("sunos") >= 0);
  }
  
  private static String computeSystemRoot() {
    String value = System.getenv("SystemRoot");
    if (value == null)
      return Strings.empty();
    return value.replaceAll("\\\\", "/");
  }

  private static String computeJavaVersion() {
    return System.getProperty("java.version", "");
  }
  
  public static String env(String paramName, File path) {
    Args.requireText(paramName, "paramName is empty");
    Args.requireNonNull(path, "path is null");
    return env(paramName, stringPath(path));
  }
  
  public static String env(String paramName, Path path) {
    Args.requireText(paramName, "paramName is empty");
    Args.requireNonNull(path, "path is null");
    return env(paramName, path.toFile());
  }
  
  public static String env(String paramName, String paramValue) {
    Args.requireText(paramName, "paramName is empty").trim();
    return format("-D%s=%s", trim(paramName), trim(paramValue));
  }
}
