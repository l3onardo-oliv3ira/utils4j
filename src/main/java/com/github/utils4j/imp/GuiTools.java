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

import static com.github.utils4j.imp.Args.requireNonNull;
import static com.github.utils4j.imp.Throwables.tryCall;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

public class GuiTools {

  private GuiTools() {}
  
  public static void mouseTracker(final Window dialog) {
    mouseTracker(dialog, 1000);
  }
  
  public static void mouseTracker(final Window dialog, long delayMillis) {
    requireNonNull(dialog, "dialog is null");
    dialog.addWindowListener(new MouseTracker(delayMillis, dialog));
  }
  
  public static void showOnMousePointer(final Window dialog) {
    setMouseLocation(dialog);
    if (!dialog.isVisible()) {
      dialog.setVisible(true);
    }
  }
  
  private static class MouseTracker extends WindowAdapter implements Runnable {
    
    private final Window dialog;
    private final long delayMillis;
    private volatile boolean running = true;
    private final Thread thread = new Thread(this, "Mouse tracker");

    private MouseTracker(long delayMillis, Window dialog) {
      this.delayMillis = delayMillis;
      this.dialog = dialog;
      this.thread.setDaemon(true);
      this.thread.setPriority(Thread.MIN_PRIORITY);
      this.thread.start();
    }
    
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
      running = false;
    }
    
    @Override
    public void run() {
      while(running)
        try {
          Thread.sleep(delayMillis);
          invokeLater(() -> {
            if (!dialog.isVisible() || !dialog.isDisplayable()) {
              running = false;
            }
            if (running) {
              setMouseLocation(dialog);
            }
          });
        }catch(Throwable e) {
          running = false;
        }
    }
  }

  public static void setMouseLocation(final Window window) {
    Args.requireNonNull(window, "dialog is null");
    if (window instanceof JFrame) {
      JFrame frame = (JFrame)window;
      if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
        return;
      }
    }
    tryCall(MouseInfo::getPointerInfo).ifPresent(mouseInfo -> {    
      final GraphicsDevice mouseDevice = mouseInfo.getDevice();
      if (mouseDevice == null)
        return;
      final GraphicsConfiguration configuration = mouseDevice.getDefaultConfiguration();
      if (configuration == null)
        return;
      final Rectangle bounds = configuration.getBounds();
      if (bounds == null)
        return;
      if (bounds.contains(window.getLocation()))
        return;
      final int width = bounds.width, height = bounds.height;
      final int xcoordinate = ((width / 2) - (window.getSize().width / 2)) + bounds.x;
      final int ycoordinate = ((height / 2) - (window.getSize().height / 2)) + bounds.y;
      window.setLocation(xcoordinate, ycoordinate);
      window.toFront();
    });
  }
}
