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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StopWatch {

  private static final Logger LOGGER = LoggerFactory.getLogger(StopWatch.class);
  
  private long total;

  private long start;

  private Logger logger;

  public StopWatch(){
    this(LOGGER);
  }

  public StopWatch(Logger logger){
    this.logger = logger;
  }

  public final Logger getLogger() {
    return this.logger;
  }
  
  public void start() {
    start = System.currentTimeMillis();
  }

  public long stop() {
    long diff = System.currentTimeMillis() - start;
    total += diff;
    return diff;
  }

  public long stop(String message){
    long time = stop();
    if (logger.isDebugEnabled()){
      logger.debug(message + ": " + time + "ms");
    }
    return time;
  }

  public void reset() {
    total = 0;
  }

  public void reset(long total) {
    this.total = total;
  }

  public long getTime() {
    return total;
  }
}
