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

import static com.github.utils4j.imp.Threads.startAsync;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class BooleanTimeout {

  private final long timeout;

  private Thread rollbackThread;

  private volatile long lastTime;

  private final AtomicBoolean value = new AtomicBoolean(false);

  public BooleanTimeout(long timeout) {
    this.timeout = timeout;
  }
  
  private void reset() {
    lastTime = System.currentTimeMillis();
  }
  
  private long diffTime() {
    return System.currentTimeMillis() - lastTime;
  }
  
  private long waitingTime() {
    return (lastTime + timeout) - System.currentTimeMillis() + 50; //50 is margin of error
  }
  
  private boolean hasTimeout() {
    return diffTime() >= timeout;
  }

  private void start() {
    if (rollbackThread == null) {
      rollbackThread = startAsync("rollback boolean to false timeout", this::roolbackToFalse);
    }
  }

  public final void setTrue() {
    value.set(true);
    isTrue();
    synchronized(value) {
      value.notifyAll();
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
  
  public final void shutdown() throws InterruptedException {
    rollbackThread.interrupt();
    rollbackThread.join();
  }
  
  private void roolbackToFalse() {
    out:
    do {
      synchronized(value) {
        while (!value.get()) {
          try {
            value.wait();
          } catch (InterruptedException e) {
            rollbackThread.interrupt();
            break out;
          }
        }
      }
      
      while(!hasTimeout()) {
        synchronized(value) {
          try {
            value.wait(waitingTime());
          } catch (InterruptedException e) {
            rollbackThread.interrupt();
            break out;
          }
        }
      }
        
      value.set(false);

    }while(!rollbackThread.isInterrupted());
    
    rollbackThread = null;

    value.set(false);
  }
  
  public static void main(String[] args) throws Throwable {
    BooleanTimeout discarting = new BooleanTimeout(5000);
    
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    String line;
    while((line = console.readLine()) != null) {
      if ("fim".equals(line)) {
        discarting.shutdown();
        break;
      }
      discarting.setTrue();
    }
    System.out.println("FIM DO PROGRAMA");
  }
}
