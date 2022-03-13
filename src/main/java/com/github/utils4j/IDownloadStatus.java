package com.github.utils4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public interface IDownloadStatus {
  
  OutputStream onNewTry(int attemptCount) throws IOException;
  
  void onStartDownload(long total) throws InterruptedException;
  
  void onEndDownload() throws InterruptedException;
  
  void onStatus(long total, long written) throws InterruptedException;

  void onDownloadFail(Throwable e) throws InterruptedException;

  Optional<File> getDownloadedFile();
}
