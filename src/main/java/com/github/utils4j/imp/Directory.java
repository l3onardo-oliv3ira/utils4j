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

import static com.github.utils4j.imp.Throwables.runQuietly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Directory {
  private Directory() {}

  public static void deleteQuietly(File file) {
    runQuietly(() -> delete(file));
  }

  public static void rmDir(Path path) throws IOException {
    rmDir(path, f -> true);
  }

  public static void mkDir(Path path) throws IOException {
    Args.requireNonNull(path, "path is null");
    mkDir(path.toFile());
  }
  
  public static void rmkDir(Path path) throws IOException {
    Directory.rmDir(path);
    Directory.mkDir(path);   
  }
  
  public static void rmkDir(File path) throws IOException {
    Args.requireNonNull(path, "path is null");
    rmkDir(path.toPath());
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

  public static boolean isSameFile(Path p1, Path p2) {
    try {
      return Files.isSameFile(p1, p2);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void delete(File file) throws IOException {
    Args.requireNonNull(file, "file is null");
    if (file.isFile())
      file.delete();
    else
      rmDir(file.toPath());
  }

  public static Path requireExists(Path input, String message) throws IOException {
    Args.requireNonNull(input, "input is null");
    requireExists(input.toFile(), message);
    return input;
  }
  
  public static File requireExists(File input, String message) throws IOException {
    Args.requireNonNull(input, "input is null");
    if (!input.exists())
      throw new IOException(message);
    return input;
  }

  public static void rmDir(Path path, Predicate<File> predicate) throws IOException {
    Args.requireNonNull(path, "path is null");
    Args.requireNonNull(predicate, "predicate is null");
    if (Files.exists(path)) {
      try (Stream<Path> walk = Files.walk(path)) {        
        walk.sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .filter(predicate)
        .forEach(File::delete);
      }
    }
  }
  
  public static void ifExists(File file, Consumer<File> consumer) {
    if (file != null && file.exists()) {
      consumer.accept(file);
    }
  }

  public static File createTempFile(String prefix) throws IOException {
    return createTempFile(prefix, ".util4j.tmp");
  }

  public static File createTempFile(String prefix, File directory) throws IOException {
    return createTempFile(prefix, ".util4j.tmp", directory);
  }

  public static File createTempFile(String prefix, String suffix) throws IOException {
    return createTempFile(prefix, suffix, null);
  }

  public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
    Args.requireNonNull(prefix, "prefix is null");
    try {
      return File.createTempFile(prefix, suffix, directory);
    }catch(Exception e) {
      throw new IOException("Não é possível criar arquivo temporário", e);
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
  
  public static void rmCopy(Path source, Path dest) throws IOException {
    Directory.rmDir(dest);
    Directory.copyFolder(source, dest);
  }

  public static void copyFolder(Path src, Path dest) throws IOException {
    Args.requireNonNull(src, "src is null");
    Args.requireNonNull(dest, "dest is null");
    try (Stream<Path> stream = Files.walk(src)) {
      stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
    }catch(RuntimeException e) {
      throw new IOException(e);
    }
  }
  
  public static String stringPath(File file) {
    return stringPath(file, false);
  }
  
  public static String stringPath(Path path) {
    return stringPath(path, false);
  }
  
  public static String stringPath(Path path, boolean quotes) {
    if (path == null)
      return quotes ? Strings.quotes("null") : "null";
    return stringPath(path.toFile(), quotes);
  }

  public static String stringPath(File file, boolean quotes) {
    if (file == null)
      return quotes ? Strings.quotes("null") : "null";
    String path;
    try {
      path = file.getCanonicalPath();
    } catch(IOException e) {
      path = file.getAbsolutePath();
    } 
    return quotes ? Strings.quotes(path) : path;    
  }

  private static void copy(Path source, Path dest) {
    try {
      Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
