package com.github.utils4j;

import java.io.File;
import java.util.List;

public interface IOfferer {

  void offer(List<File> files) throws InterruptedException;

}