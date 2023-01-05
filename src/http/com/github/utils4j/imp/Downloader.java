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

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpHeaders;

import com.github.utils4j.IDownloadStatus;
import com.github.utils4j.IDownloader;
import com.github.utils4j.IGetCodec;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class Downloader implements IDownloader {
  
  private final String rootUri;  

  private final IGetCodec codec;
  
  private final BehaviorSubject<HttpUriRequest> status = BehaviorSubject.create();

  public Downloader(String rootUri, CloseableHttpClient client) {
    this.rootUri = Args.requireNonNull(rootUri, "root Uri is null").trim();
    this.codec = new DownloaderCodec(client);
  }
  
  @Override
  public final String match(String path) {
    return Strings.encodeHtmlSpace(rootUri + Strings.trim(path));
  }  
  
  @Override
  public final Observable<HttpUriRequest> newRequest() {
    return status;
  }
  
  @Override
  public final void download(String uri, IDownloadStatus status) throws IOException, InterruptedException {
    Args.requireNonNull(uri, "uri is null");
    Args.requireNonNull(status, "status is null");
    final String fullUrl = match(uri);
    try {
      codec.get(() -> createRequest(fullUrl), status);
    } catch (InterruptedException e) {
      throw e;
    } catch (IOException e) {
      checkInterrupt(e);
      throw e;
    } catch (Exception e) {
      checkInterrupt(e);
      throw new IOException("Unabled to download from url: " + fullUrl, e);
    }
  }
  
  protected static void checkInterrupt(Exception e) throws InterruptedException {
    if (Thread.currentThread().isInterrupted() || "Request aborted".equals(e.getMessage())) {
      throw new InterruptedException("Download cancelado! \n\tcause: " + Throwables.rootMessage(e));
    } 
  }
  
  private HttpGet createRequest(String fullUrl) throws URISyntaxException {
    HttpGet r = new HttpGet(fullUrl);
    //this is very important (akamai blocking if not set header ACCEPT_ENCODING)
    r.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
    r.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
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
