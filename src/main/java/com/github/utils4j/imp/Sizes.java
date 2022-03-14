package com.github.utils4j.imp;

public enum Sizes {
  _1B(1, "B"),
  _1KB(_1B.size * 1024, "KB"),
  _1MB(_1KB.size * 1024, "MB"),
  _1GB(_1MB.size * 1024, "GB"),
  _1TB(_1GB.size * 1024, "TB"),
  _1PB(_1TB.size * 1024, "PB");
  
  private final long size;
  private final String label;
  
  private Sizes(long size, String label) {
    this.size = size;
    this.label = label;
  }
  
  public static String defaultFormat(long size) {
    if (size < _1KB.size)
      return _1B.format(size);
    if (size < _1MB.size)
      return _1KB.format(size);
    if (size < _1GB.size)
      return _1MB.format(size);
    if (size < _1TB.size)
      return _1GB.format(size);
    return _1TB.format(size);
  }

  String format(long size) {
    return String.format("%.2f %s", ((double)size / this.size), label);
  }
}
