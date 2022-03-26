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

import static com.github.utils4j.imp.Threads.startAsync;
import static com.github.utils4j.imp.Throwables.tryCall;
import static com.github.utils4j.imp.Throwables.tryRuntime;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.function.Procedure;
import com.github.utils4j.imp.function.Supplier;

public class SwingTools {
  
  private SwingTools() {}
  
  public static boolean isTrue(Supplier<Boolean> supplier){
    return invokeAndWait(supplier).orElse(Boolean.FALSE);
  }
  
  public static boolean invokeAndWait(Runnable code) {
    return invokeAndWait(code, false);
  }

  public static boolean invokeAndWait(Runnable code, boolean defaultIfFail) {
    Args.requireNonNull(code, "code is null");    
    Procedure<Boolean, ?> p = SwingUtilities.isEventDispatchThread() ? 
      () -> { code.run(); return true; } : 
      () -> { SwingUtilities.invokeAndWait(code); return true;};
    return tryCall(p, defaultIfFail);
  }

  public static void invokeLater(Runnable code) {
    Args.requireNonNull(code, "code is null");
    if (SwingUtilities.isEventDispatchThread()) {
      code.run();
    } else {
      SwingUtilities.invokeLater(code);
    }
  }

  public static <T> Optional<T> invokeAndWait(Supplier<T> supplier){
    Args.requireNonNull(supplier, "supplier is null");
    Procedure<Optional<T>, ?> p = SwingUtilities.isEventDispatchThread() ? 
      () -> ofNullable(supplier.get()) : 
      () -> {
        final AtomicReference<T> ref = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> tryRuntime(() -> ref.set(supplier.get())));
        return ofNullable(ref.get());
      };
    return tryCall(p, empty());
  }
  
  public static void setFixedMinimumSize(Window window, Dimension dimension) {
    Args.requireNonNull(window, "window is null");
    Args.requireNonNull(dimension, "dimension is null");
    window.setMinimumSize(dimension);
    window.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        Dimension windowDimension = window.getSize();
        Dimension minimumDimension = window.getMinimumSize();
        windowDimension.width = Math.max(windowDimension.width, minimumDimension.width);
        windowDimension.height =  Math.max(windowDimension.height, minimumDimension.height);
        window.setSize(windowDimension);
      }
    });    
  }

  public static void showToFront(Window window) {
    Args.requireNonNull(window, "window is null");
    boolean top = window.isAlwaysOnTop();
    window.setAlwaysOnTop(true);
    window.setVisible(true); 
    window.toFront();    
    startAsync(() -> invokeLater(() -> window.setAlwaysOnTop(top)), 500); //force to on top   
  }
}
