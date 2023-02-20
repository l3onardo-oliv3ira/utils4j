/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


package com.github.utils4j.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.utils4j.IDownloadStatus;

public class DownloadStatus implements IDownloadStatus {

  protected static final Logger LOGGER = LoggerFactory.getLogger(IDownloadStatus.class);
  
  private File output;

  private OutputStream out;

  private boolean online = false;
  
  private final boolean rejectEmpty;
  
  public DownloadStatus() {
    this(true);
  }
  
  public DownloadStatus(boolean rejectEmpty) {
    this(rejectEmpty, null);
  }
  
  public DownloadStatus(File output) {
    this(true, output);
  }

  public DownloadStatus(boolean rejectEmpty, File output) {
    this.rejectEmpty = rejectEmpty;
    this.output = output;
  }
  
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
  
  @Override
  public final OutputStream onNewTry() throws IOException {
    checkIfOffline();
    if (output == null) {
      output = Directory.createTempFile("downloaded");
    }
    out = new FileOutputStream(output) {
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
  public final void onDownloadFail(Throwable e) {
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
    if (output != null) {
      if (force || (rejectEmpty && output.length() == 0)) {
        output.delete();
        output = null;
      }
    }
  }
  
  @Override
  public final Optional<File> getDownloadedFile() {
    checkIfOffline();
    return Optional.ofNullable(output);
  }

  protected void onStepStart(long total) throws InterruptedException {}
  
  protected void onStepStatus(long written) throws InterruptedException {}
  
  protected void onStepEnd() throws InterruptedException {}
  
  protected void onStepFail(Throwable e) {
    LOGGER.warn("step fail", e);
  }
}
