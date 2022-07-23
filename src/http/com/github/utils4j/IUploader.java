package com.github.utils4j;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import io.reactivex.Observable;

public interface IUploader {
  Observable<HttpUriRequest> newRequest();

  void upload(String url, IUploadStatus status) throws IOException;
}
