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

import java.net.ProxySelector;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
import org.apache.hc.core5.util.Timeout;

public final class DefaultHttpClientCreator {  

  private static final Timeout _1m = Timeout.ofMinutes(1);
  private static final Timeout _3m = Timeout.ofMinutes(3);
  private static final Timeout _30s = Timeout.ofSeconds(30);
  
  private DefaultHttpClientCreator() {}
  
  public static CloseableHttpClient create(String userAgent) throws Exception {
    return HttpClients.custom()
      .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
      .evictExpiredConnections()
      .evictIdleConnections(_1m)
      .setUserAgent(userAgent)    
      .setDefaultRequestConfig(RequestConfig.custom()
        .setResponseTimeout(_30s)
        .setConnectTimeout(_3m)    
        .setConnectionKeepAlive(_3m)
        .setConnectionRequestTimeout(_3m)
        .setCookieSpec(StandardCookieSpec.IGNORE).build()
      ).build();
  }
}
