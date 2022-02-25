package com.github.utils4j;

import java.io.IOException;

@FunctionalInterface
public interface ITextReader {
  <T> T read(String text) throws IOException;
}
