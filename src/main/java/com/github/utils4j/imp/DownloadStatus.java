package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryCall;
import static java.io.File.createTempFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import com.github.utils4j.IDownloadStatus;

public class DownloadStatus implements IDownloadStatus {

  private File file;

  private boolean online = false;
  
  private final boolean rejectEmpty;
  
  private final Optional<File> saveAt;

  private OutputStream out;

  private void checkIfOffline() {
    throwIfOnlineIs(true, "status is online");
  }
  
  private void checkIfOnline() {
    throwIfOnlineIs(false, "status is offline");
  }

  private void throwIfOnlineIs(boolean status, String message) {
    if (online == status) {
      throw new IllegalStateException(message);
    }
  }
  
  public DownloadStatus() {
    this(true, null);
  }
  
  public DownloadStatus(File saveAs) {
    this(true, saveAs);
  }

  public DownloadStatus(boolean rejectEmpty, File saveAt) {
    this.rejectEmpty = rejectEmpty;
    this.saveAt = Optional.ofNullable(saveAt);
  }

  @Override
  public final OutputStream onNewTry(int attemptCount) throws IOException {
    checkIfOffline();
    file = saveAt.orElseGet(() -> tryCall(() -> createTempFile("downloaded_tmp", ".utils4j.tmp"), new File("*unabled to create temp file*")));
    out = new FileOutputStream(file) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        }finally {
          DownloadStatus.this.out = null;
          DownloadStatus.this.checkIfEmpty(false);
          DownloadStatus.this.online = false;
        }
      }
    };
    this.online = true;
    return out;
  }
  
  @Override
  public final void onStartDownload(long total) throws InterruptedException {
    checkIfOnline();
    onStepStart(total);
  }
  
  @Override
  public final void onStatus(long total, long written) throws InterruptedException {
    checkIfOnline();
    onStepStatus(written);
  }

  @Override
  public final void onDownloadFail(Throwable e) throws InterruptedException  {
    Streams.closeQuietly(out);
    checkIfEmpty(true);
    onStepFail(e);
  }

  @Override
  public final void onEndDownload() throws InterruptedException {
    Streams.closeQuietly(out);
    onStepEnd();
  }

  private void checkIfEmpty(boolean force) {
    if (file != null) {
      if (force || (rejectEmpty && file.length() == 0)) {
        file.delete();
        file = null;
      }
    }
  }
  
  @Override
  public final Optional<File> getDownloadedFile() {
    checkIfOffline();
    return Optional.ofNullable(file);
  }

  protected void onStepStart(long total) throws InterruptedException {}
  
  protected void onStepStatus(long written) throws InterruptedException { }
  
  protected void onStepEnd() throws InterruptedException {}
  
  protected void onStepFail(Throwable e) throws InterruptedException {}

}
