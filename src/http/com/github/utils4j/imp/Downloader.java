package com.github.utils4j.imp;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.github.utils4j.IDownloadStatus;
import com.github.utils4j.IDownloader;
import com.github.utils4j.IGetCodec;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class Downloader implements IDownloader {
  
  private IGetCodec codec;
  
  private final BehaviorSubject<HttpUriRequest> status = BehaviorSubject.create();  

  Downloader(CloseableHttpClient client) {
    this.codec = new DownloaderCodec(client);
  }
  
  @Override
  public final Observable<HttpUriRequest> newRequest() {
    return status;
  }
  
  @Override
  public final void download(String uri, IDownloadStatus status) throws IOException {
    Args.requireNonNull(uri, "uri is null");
    Args.requireNonNull(status, "status is null");
    try {
      codec.get(() -> createRequest(uri), status);
    } catch (IOException e) {
      throw e;
    } catch (Throwable e) {
      throw new IOException("Unabled to download from url: " + uri, e);
    }
  }
  
  private HttpGet createRequest(String uri) {
    HttpGet r = new HttpGet(uri);
    status.onNext(r);
    return r;
  }

  private static class DownloaderCodec extends WebCodec<Void> {
    
    public DownloaderCodec(CloseableHttpClient client) {
      super(client);
    }

    @Override
    protected Exception launch(String message, Exception e) {
      return new IOException(message, e);
    }

    @Override
    protected Void success() {
      return null;
    }    
  }  
}
