package com.github.utils4j.imp;

public class RetryTimeoutException extends Exception {
  
  private static final long serialVersionUID = 1L;

  public RetryTimeoutException(Throwable cause){
    super(cause);
  }
}