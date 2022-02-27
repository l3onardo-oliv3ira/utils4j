package com.github.utils4j.imp;

public class NotImplementedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotImplementedException() {
    super();
  }
  
  public NotImplementedException(String message) {
    super(message);
  }
  
  public NotImplementedException(String message, Throwable cause) {
    super(message, cause);
  }
}
