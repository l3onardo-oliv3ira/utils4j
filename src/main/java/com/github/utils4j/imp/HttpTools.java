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

import static com.github.utils4j.imp.Throwables.tryRun;

import java.io.IOException;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class HttpTools {

  private static CloseableHttpClient TOUCH_CLIENT = HttpClientBuilder.create()
    .disableAuthCaching()
    .disableAutomaticRetries()
    .disableConnectionState()
    .disableContentCompression()
    .disableCookieManagement()
    .disableRedirectHandling()
    .evictExpiredConnections() //TODO we have to go back here!
    .evictIdleConnections(TimeValue.ofMinutes(1)) //TODO we have to go back here!
    .setDefaultRequestConfig(RequestConfig.custom()
      .setCookieSpec(StandardCookieSpec.IGNORE).build())
    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
        .setMaxConnPerRoute(2)
        .setMaxConnTotal(2)
        .setValidateAfterInactivity(TimeValue.ofSeconds(10))
        .build())
    .build();
      
  private HttpTools() {}
  
  public static void touchQuietly(String uri) {
    touchQuietly(uri, "HttpTools");
  }
  
  public static void touchQuietly(String uri, String userAgent) {
    tryRun(() -> touch(uri, userAgent));
  }

  public static void touch(String uri) throws IOException {
    touch(uri, "HttpTools"); 
  }

  public static void touch(String uri, String userAgent) throws IOException {
    touch(uri, userAgent, Timeout.ofSeconds(5));
  }

  public static void touch(String uri, String userAgent, Timeout timeout) throws IOException {
    Args.requireNonNull(uri, "uri is null");
    Args.requireNonNull(userAgent, "userAgent is null");
    Args.requireNonNull(timeout, "timeout is null");
    Request.get(uri).connectTimeout(timeout).responseTimeout(timeout).execute(TOUCH_CLIENT).discardContent();
  }
}
