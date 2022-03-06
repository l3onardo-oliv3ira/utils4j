package com.github.utils4j;

import java.io.File;
import java.util.List;

public interface IFilePacker extends IThreadContext {

  void reset();

  List<File> filePackage() throws InterruptedException;
  
}