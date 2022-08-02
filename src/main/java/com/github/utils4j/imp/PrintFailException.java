package com.github.utils4j.imp;

public class PrintFailException extends Exception {
  public PrintFailException(Exception e) {
    super(e);
  }

  public PrintFailException(String message, Exception e) {
    super(message, e);
  }
}
