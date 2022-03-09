package com.github.utils4j;

public interface ILifeCycle<E extends Exception> {

  boolean isStarted();
  
  void start() throws E ;

  void stop() throws E;
  
  default void stop(boolean kill) throws E {
    stop();
  }
  
  default void stop(long timeout) throws E {
    stop();
  }
}
