package com.github.utils4j;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import io.reactivex.Observable;

public interface IDownloader {
  Observable<HttpUriRequest> newRequest();

  void download(String url, IDownloadStatus status) throws IOException;
}
