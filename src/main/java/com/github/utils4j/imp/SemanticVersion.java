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

import static com.github.utils4j.imp.Strings.hasText;

import java.io.IOException;

public class SemanticVersion {

  public static SemanticVersion from(int major, int minor, int patch) {
    return new SemanticVersion(major, minor, patch);
  }
  
  public static SemanticVersion from(String version) throws IOException {
    checkFormat(!hasText(version), version);
    
    String[] members = version.split("\\.");
    checkFormat(members.length != 3, version);
      
    int major = Strings.toInt(members[0], -1);
    checkFormat(major <= 0, version);
    
    int minor = Strings.toInt(members[1], -1);
    checkFormat(minor < 0, version);

    int patch = Strings.toInt(members[2], -1);
    checkFormat(patch < 0, version);
    return from(major, minor, patch);
  }
  
  private static void checkFormat(boolean condition, String version) throws IOException {
    if (condition) {
      throw new IOException("Versão em formato inválido '" + version + "'");
    }
  }

  private final int major, minor, patch;

  protected SemanticVersion(int major, int minor, int patch) {
    this.major = Args.requirePositive(major, "major is <= 0");
    this.minor = Args.requireZeroPositive(minor, "minor is < 0");
    this.patch = Args.requireZeroPositive(patch, "patch is < 0");
  }
  
  @Override
  public final String toString() {
    return major + "." + minor + "." + patch;
  }
  
  public final String fullString() {
    return toString() + ".0";
  }
  
  public final int major() {
    return major;
  }
  
  public final int minor() {
    return minor;
  }
  
  public final int patch() {
    return patch;
  }

  public final int match(SemanticVersion version) {
    if (major > version.major())
      return 1;    
    if (major < version.major())
      return  -1;
    //at this line major is equals  (major == VersionStatus.major())
    if (minor > version.minor())
      return 1;
    if (minor < version.minor())
      return -1;
    //at this line major is equals  (major == VersionStatus.minor())
    return patch - version.patch;
  }  
}
