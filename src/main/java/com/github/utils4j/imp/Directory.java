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
  
  public static void clean(Path path) throws IOException {
    clean(path, (f) -> true);
  }
  
  public static void clean(Path path, Predicate<File> predicate) throws IOException {
    try (Stream<Path> walk = Files.walk(path)) {        
      walk.sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .filter(predicate)
        .forEach(File::delete);
    }
  }
  
  public static void requireFolder(Path path) throws IOException {
    Args.requireNonNull(path, "path is null");
    File f = path.toFile();
    if (f.exists() && f.isDirectory())
      return;
    f.delete();
    if (!f.mkdirs()) {
      throw new IOException("Unabled to create folder " + f.getAbsolutePath());
    }
  }
}
