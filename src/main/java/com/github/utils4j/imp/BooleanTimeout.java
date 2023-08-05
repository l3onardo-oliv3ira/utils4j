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

import static com.github.utils4j.imp.Threads.startDaemon;
import static com.github.utils4j.imp.Throwables.runQuietly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Um BooleanTimeout é um objeto que após ganhar o valor TRUE, voltará automaticamente a ter um valor FALSE
 * após um período de timeout desde a última vez que foi consultado (isTrue)
 * @author Leonardo
 *
 */
public class BooleanTimeout {

  private static final Logger LOGGER = LoggerFactory.getLogger(BooleanTimeout.class);
  
  private static final int DEFAULT_TIMEOUT = 2000;
  
  private final String name;

  private final long timeout;

  private Thread rollbackThread;

  private volatile long lastTime;
  
  private final Runnable timeoutCode;

  private final AtomicBoolean value = new AtomicBoolean(false);
  
  private final List<Runnable> ephemeralTimeoutCodes = new ArrayList<>(4);

  public BooleanTimeout(String name) {
    this(name, DEFAULT_TIMEOUT);
  }
  
  public BooleanTimeout(String name, Runnable timeoutCode) {
    this(name, DEFAULT_TIMEOUT, timeoutCode);
  }
  
  public BooleanTimeout(String name, long timeout) {
    this(name, timeout, () -> {});
  }

  public BooleanTimeout(String name, long timeout, Runnable timeoutCode) {
    this.name = Args.requireText(name, "name is empty");
    this.timeout = Args.requireZeroPositive(timeout, "timeout is negative");
    this.timeoutCode = Args.requireNonNull(timeoutCode, "timeoutCode is null");
  }  

  public final long getTimeout() {
    return timeout;
  }
  
  private void reset() {
    lastTime = System.currentTimeMillis();
  }
  
  private long deadline() {
    return lastTime + timeout;
  }

  private long deadlineRemaining() {
    return deadline() - System.currentTimeMillis();
  }
  
  private boolean hasTimeout() {
    return deadline() <= System.currentTimeMillis();
  }

  private synchronized void start() {
    if (rollbackThread == null) {
      rollbackThread = startDaemon(name + ": rollback boolean to false timeout", this::roolbackToFalse);
    }
  }

  public final void setTrue() {
    value.set(true);
    isTrue();
    synchronized(value) {
      value.notifyAll();
    }
  }
  
  public final void setTrue(Runnable timeoutCode) {
    synchronized(ephemeralTimeoutCodes) {
      setTrue();
      addEphemeralTimeout(timeoutCode);
    }
  }

  private void addEphemeralTimeout(Runnable timeoutCode) {
    if (timeoutCode != null) {      
      ephemeralTimeoutCodes.add(timeoutCode);
    }
  }
  
  public final boolean isTrue() {
    reset();
    boolean state = value.get();
    if (state) {
      start();
    }
    return state;
  }
  
  public synchronized void shutdown() {
    if (rollbackThread != null) {
      rollbackThread.interrupt();
      rollbackThread = null;
    }
  }
  
  private void roolbackToFalse() {
    out:
    do {
      synchronized(value) {
        while (!value.get()) {
          try {
            value.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break out;
          }
        }
      }
      
      while(!hasTimeout()) {
        long waitingTime = deadlineRemaining();
        if (waitingTime > 0) {
          synchronized(value) {
            try {
              value.wait(waitingTime + 20); //20 is margin of error
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break out;
            }
          }
        }
      }
        
      value.set(false);
      
      LOGGER.debug(name + ": reset to false");

      runTimeout();
      
    } while(!Thread.currentThread().isInterrupted());
  }

  private void runTimeout() {
    runQuietly(timeoutCode::run);
    
    List<Runnable> codes;
    synchronized(ephemeralTimeoutCodes) {
      codes = new ArrayList<>(ephemeralTimeoutCodes.size());
      codes.addAll(ephemeralTimeoutCodes);
      ephemeralTimeoutCodes.clear();        
    }

    for(Runnable r: codes) {
      runQuietly(r::run);
    }
  }
}
