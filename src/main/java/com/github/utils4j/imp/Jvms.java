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

//$MAJOR.$MINOR.$SECURITY.$PATCH for java 9+
//1.$MAJOR.$MINOR_$UPDATE-$BUILD


public class Jvms {
  private Jvms() {}

  public static String OS_NAME = System.getProperty("os.name").toLowerCase();

  public static final String SYSTEM_ROOT = computeSystemRoot();

  public static final String JAVA_VERSION = computeJavaVersion();

  public static final int JAVA_MAJOR_VERSION = computeJavaMajorVersion();
  
  public static final int JAVA_MINOR_VERSION = computeMinorVersion();

  public static final int JAVA_UPDATE_VERSION = computeJavaUpdateVersion();
  
  public static boolean isWindows() {
    return (OS_NAME.indexOf("win") >= 0);
  }
  
  public static boolean isMac() {
    return (OS_NAME.indexOf("mac") >= 0);
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

  private static int computeJavaMajorVersion() {
    String full = System.getProperty("java.version", "");
    if (full.startsWith("1."))
      return Strings.toInt(full.substring(2, full.indexOf(".", 2)), -1);
    return Strings.toInt(full.substring(0, full.indexOf(".")), -1);
  }

  private static int computeMinorVersion() {
    String full = System.getProperty("java.version", "");
    if (full.startsWith("1."))
      return Strings.toInt(full.substring(full.lastIndexOf(".") + 1, full.lastIndexOf("_")), -1);
    int s = full.indexOf(".") + 1;
    return Strings.toInt(full.substring(s, full.indexOf(".", s)), -1);
  }
  
  private static int computeJavaUpdateVersion() {
    String full = System.getProperty("java.version");
    int s, e;
    if (full.startsWith("1.")) {
      s = full.indexOf("_");
      if (s < 0)
        return 0;
      e = full.lastIndexOf('-');
      if (e < 0)
        e = full.length();
      return Strings.toInt(full.substring(s + 1), e);
    }
    s = full.indexOf(".") + 1;
    s = full.indexOf(".", s) + 1;
    e = full.indexOf(".", s);
    return Strings.toInt(full.substring(s), e);
  }

  public static void print() {
    System.out.println("OS_NAME: " + OS_NAME);
    System.out.println("SYSTEM_ROOT: " + SYSTEM_ROOT);
    System.out.println("JAVA_VERSION: " + JAVA_VERSION);
    System.out.println("JAVA_MAJOR_VERSION: " + JAVA_MAJOR_VERSION);
    System.out.println("JAVA_MINOR_VERSION: " + JAVA_MINOR_VERSION);
    System.out.println("JAVA_UPDATE_VERSION: " + JAVA_UPDATE_VERSION);
  }
  
  public static void main(String[] args) {
    print();
  }
}
