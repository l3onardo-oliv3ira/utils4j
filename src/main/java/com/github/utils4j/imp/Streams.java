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


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.utils4j.IAcumulator;
import com.github.utils4j.IConstants;

public class Streams {
  private Streams() {
  }

  public static void closeQuietly(Closeable c) {
    try {
      c.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void closeQuietly(AutoCloseable c) {
    try {
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void closeQuietly(OutputStream s) {
    if (s != null) {
      flushQuietly(s);
      closeQuietly((AutoCloseable)s);
    }
  }

  public static void flushQuietly(OutputStream s) {
    if (s != null) {
      try {
        s.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void consumeQuietly(InputStream s) {
    if (s != null) {
      try {        
        s.skip(Long.MAX_VALUE);
      }catch(IOException e) {
        e.printStackTrace();
      }finally {
        closeQuietly(s);
      }
    }
  }

  public static CompletableFuture<String> readOutStream(InputStream is) {
    return readOutStream(is, IConstants.DEFAULT_CHARSET);
  }

  public static CompletableFuture<String> readOutStream(InputStream is, Charset charset) {
    return readOutStream(is, charset, new LineAppender());
  }

  public static CompletableFuture<String> readOutStream(InputStream is, Charset charset, IAcumulator<String> acumulator) {
    return CompletableFuture.supplyAsync(() -> {
      Thread thread = Thread.currentThread();
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
        String inputLine;
        while (!thread.isInterrupted() && (inputLine = br.readLine()) != null) {
          acumulator.accept(inputLine);
        }
        return acumulator.get();
      } catch (IOException e) {
        return acumulator.handleFail(e);
      }
    });
  }

  public static String md5(File file) throws IOException {
    try(InputStream fis =  new FileInputStream(file)){
      return DigestUtils.md5Hex(fis);
    }
  }

  public static String sha1(File file) throws IOException {
    try(InputStream fis =  new FileInputStream(file)){
      return DigestUtils.sha1Hex(fis);
    }
  }

  public static String sha1(byte[] input) {
    return DigestUtils.sha1Hex(input);
  }

  public static boolean isSame(Path path1, Path path2) {
    try {
      return Files.isSameFile(path2, path1);
    } catch (IOException e) {
      return false; //we have to go back here!
    }
  }

  public static boolean isSame(String path1, String path2) {
    return isSame(Paths.get(path1), Paths.get(path2));
  }

  public static byte[] fromResourceQuietly(String name) {
    try(InputStream is = Streams.class.getResourceAsStream(name)) {
      return fromStream(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] fromResource(String name) throws IOException {
    try(InputStream is = Streams.class.getResourceAsStream(name)) {
      return fromStream(is);
    }
  }

  public static byte[] fromStream(InputStream is) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[1024];
    int read;
    while ((read = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, read);
    }
    return buffer.toByteArray();
  }

  public static void resourceLines(String name, Consumer<Stream<String>> consumer) {
    resourceLines(name, consumer, IConstants.UTF_8);
  }

  public static void resourceLines(String name, Consumer<Stream<String>> consumer, Charset charsetName) {
    try(BufferedReader r = new BufferedReader(new InputStreamReader(Streams.class.getResourceAsStream(name), charsetName))) {
      consumer.accept(r.lines());
    }catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void copy(byte[] data, OutputStream out) throws IOException {
    copy(data, out, 32 * 1024); 
  }

  public static void copy(byte[] data, OutputStream out, int bufferSize) throws IOException {
    try(InputStream input = new ByteArrayInputStream(data)) {
      copy(input, out, bufferSize);
    }
  }

  public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
    byte[] buffer = new byte[bufferSize];
    int read;
    while((read = input.read(buffer)) > 0) {
      output.write(buffer, 0, read);
    }
  }
}
