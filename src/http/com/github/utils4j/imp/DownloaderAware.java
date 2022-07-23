package com.github.utils4j.imp;

import com.github.utils4j.IDownloader;

public class DownloaderAware {
  protected final IDownloader downloader;

  protected DownloaderAware(IDownloader downloader) {
    this.downloader = Args.requireNonNull(downloader, "downloader is null");
  }
}
