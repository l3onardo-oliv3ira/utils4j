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

import static com.github.utils4j.imp.Throwables.rootMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.github.utils4j.IConstants;
import com.github.utils4j.IDownloadStatus;
import com.github.utils4j.IResultChecker;
import com.github.utils4j.ISocketCodec;
import com.github.utils4j.imp.function.IProvider;

public abstract class WebCodec<R> implements ISocketCodec<HttpPost, R> {

  protected boolean isSuccess(int code) {
    return (code >= HttpStatus.SC_SUCCESS && code < HttpStatus.SC_REDIRECTION) || code == HttpStatus.SC_NOT_MODIFIED; 
  }
  
  protected final CloseableHttpClient client;
  
  protected WebCodec(CloseableHttpClient client) {
    this.client = Args.requireNonNull(client, "client is null");
  }
  
  @Override
  public R post(final IProvider<HttpPost> provider, IResultChecker checkResults) throws Exception {
    try {      
      final HttpPost post = provider.get();
      
      try(CloseableHttpResponse response = client.execute(post)) {
      
        final int code = response.getCode();

        String responseText = Strings.empty();

        HttpEntity entity = response.getEntity();
        
        if (entity != null) {
          try {
            responseText = EntityUtils.toString(entity, IConstants.DEFAULT_CHARSET); //can return null
          } catch (ParseException | IOException e) {
            throw launch("Falha na leitura de entity - HTTP Code: " + code, e);
          } finally {
            EntityUtils.consumeQuietly(entity);  
          }

          if (responseText == null) {
            responseText = Strings.empty();
          }
        }
        
        if (isSuccess(code)) {
          handleSuccess(checkResults, responseText, code);
        } else {
          handleFail(checkResults, responseText, code);
          throw launch("Servidor retornando - HTTP Code: " + code + " Content: \n" + responseText);
        }
        
        return success();
      }

    } catch(CancellationException e) {
      throw launch("Os dados não foram enviados ao servidor. Operação cancelada!\n\tcause: " + rootMessage(e));
    }
  }

  protected void handleSuccess(IResultChecker checkResults, String responseText, int httpCode) throws Exception {
    checkResults.handle(responseText, httpCode);
  }
 
  protected void handleFail(IResultChecker checkResults, String responseText, int httpCode) throws Exception {}
  
  @Override
  public void get(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
    
    try(OutputStream output = status.onNewTry()) {
      
      final HttpGet get = provider.get();
      
      try(CloseableHttpResponse response = client.execute(get)) {
        int code = response.getCode();

        HttpEntity entity = response.getEntity();
        if (entity == null) {
          throw launch("Servidor não foi capaz de retornar dados. (entity is null) - HTTP Code: " + code);
        }
      
        try {
          if (!isSuccess(code)) { 
            throw launch("Servidor retornando - HTTP Code: " + code);
          }
          
          try {
            final long total = entity.getContentLength();
            final InputStream input = entity.getContent();
            
            status.onStartDownload(total);
            final byte[] buffer = new byte[128 * 1024];
            
            status.onStatus(total, 0);
            for(int length, written = 0; (length = input.read(buffer)) > 0; status.onStatus(total, written += length))
              output.write(buffer, 0, length);
            status.onEndDownload();
              
          } catch(InterruptedException e) {     
            throw new InterruptedException("Download interrompido - HTTP Code: " + code + "\n\tcause: " + rootMessage(e));
          } catch(Exception e) {
            throw launch("Falha durante o download do arquivo - HTTP Code: " + code, e);
          }
        } finally {
          EntityUtils.consumeQuietly(entity);
        }
      } finally {
        get.clear();
      }
    } catch (Exception e) {
      status.onDownloadFail(e);
      throw e;
    }
  }
  
  @Override
  public String get(IProvider<HttpGet> provider) throws Exception {
    final HttpGet get = provider.get();
    
    try(CloseableHttpResponse response = client.execute(get)) {
      int code = response.getCode();

      HttpEntity entity = response.getEntity();
      if (entity == null) {
        throw launch("Servidor não foi capaz de retornar dados. (entity is null) - HTTP Code: " + code);
      }
    
      try {
        if (!isSuccess(code)) { 
          throw launch("Servidor retornando - HTTP Code: " + code);
        }
        
        InputStream input = entity.getContent();
        
        if (input == null) {
          return Strings.empty();
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(input, IConstants.UTF_8))) {
          return br.lines().collect(Collectors.joining("\n"));
        }
        
      } catch(InterruptedException e) {     
        throw new InterruptedException("Requisição GET interrompida - HTTP Code: " + code + "\n\tcause: " + rootMessage(e));
      } catch(Exception e) {
        throw launch("Falha durante o requisição GET - HTTP Code: " + code, e);
      } finally {
        EntityUtils.consumeQuietly(entity);
      }
    } finally {
      get.clear();
    }
  }
  
  protected final Exception launch(String message) {
    return launch(message, null);
  }
  
  protected abstract Exception launch(String message, Exception e);

  protected abstract R success();
}
