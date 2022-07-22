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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Directory {
  private Directory() {}
  
  public static void rmDir(Path path) throws IOException {
    rmDir(path, (f) -> true);
  }
  
  public static void mkDir(Path path) throws IOException {
    Args.requireNonNull(path, "path is null");
    mkDir(path.toFile());
  }
  
  public static void requireDirectory(File dir, String message) throws IOException {
    Args.requireNonNull(dir, "input is null");
    if (!dir.isDirectory())
      throw new IOException(message);
  }
  
  public static File requireNotExists(File input, String message) throws IOException {
    Args.requireNonNull(input, "input is null");
    if (input.exists()) 
      throw new IOException(message);
    return input;
  }
  
  public static File requireExists(File input, String message) throws IOException {
    Args.requireNonNull(input, "input is null");
    if (!input.exists())
      throw new IOException(message);
    return input;
  }
  
  public static void rmDir(Path path, Predicate<File> predicate) throws IOException {
    try (Stream<Path> walk = Files.walk(path)) {        
      walk.sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .filter(predicate)
        .forEach(File::delete);
    }
  }

  public static void mkDir(File folder) throws IOException {
    Args.requireNonNull(folder, "folder is null");
    if (folder.exists()) {
      if (folder.isDirectory())
        return;      
      if (!folder.delete())
        throw new IOException("Unabled to delete file " + folder);
    }
    if (!folder.mkdirs()) {
      throw new IOException("Unabled to create directory tree  " + folder);
    }
  }  
}
