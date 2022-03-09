package com.github.utils4j;

import java.io.File;
import java.util.List;

public interface IFilePacker<T extends Exception> extends ILifeCycle<T> {

  void reset();

  List<File> filePackage() throws InterruptedException;
  
}