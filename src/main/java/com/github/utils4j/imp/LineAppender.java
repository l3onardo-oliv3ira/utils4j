package com.github.utils4j.imp;

import java.io.IOException;

import com.github.utils4j.IAcumulator;

public class LineAppender implements IAcumulator<String> {
  private final StringBuilder sb = new StringBuilder();

  @Override
  public String get() {
    return sb.toString();
  }

  @Override
  public void accept(String line) {
    sb.append(line).append(System.lineSeparator());
  }

  @Override
  public String handleFail(IOException e) {
    return get();
  }
}