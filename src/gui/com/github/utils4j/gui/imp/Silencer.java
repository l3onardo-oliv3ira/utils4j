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


package com.github.utils4j.gui.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Silencer {
  
  private static Logger LOGGER = LoggerFactory.getLogger(Silencer.class);
  
  private Silencer() {}
  
  public static void failAs(boolean silent, String message, Exception e) {
    failAs(silent, message, e, "");
  }

  public static void failAs(boolean silent, String message, Exception e, String detail) {
    runAs(silent, () -> {
      LOGGER.warn(message, e);
      ExceptionAlert.show(message, detail, e);
    });
  }
  
  public static void infoAs(boolean silent, String message) {
    runAs(silent, () -> {
      LOGGER.info(message);
      MessageAlert.showInfo(message);
    });
  }

  public static void runAs(boolean silent, Runnable r) {
    if (!silent && r != null) {
      r.run();
    }
  }
}
