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

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;

import java.awt.Image;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public final class CancelAlert {

  public static String CANCELED_OPERATION_MESSAGE = "Operação em cancelada!";

  public static String CANCELING_OPERATION_MESSAGE = "Operação em cancelamento! Aguarde...";
  
  private static final AtomicBoolean visible = new AtomicBoolean(false);
  
  public static void show() {
    show(null);
  }
  
  public static void show(Image icon) {
    invokeLater(() -> display(icon));
  }

  public static void showWaiting() {
    showWaiting(null);
  }
  
  public static void showWaiting(Image icon) {
    invokeLater(() -> displayWaiting(icon));
  }

  public static boolean display() {
    return display(null);
  }

  public static boolean display(Image icon) {
    return display(CANCELED_OPERATION_MESSAGE, icon);
  }
  
  public static boolean displayWaiting() {
    return displayWaiting(null);
  }
  
  public static boolean displayWaiting(Image icon) {
    return display(CANCELING_OPERATION_MESSAGE, icon);
  }
  
  private static boolean display(String message, Image icon) {
    if (!visible.getAndSet(true)) {
      return new CancelAlert(message).reveal(icon);
    }
    return false;
  }
  
  private static final String[] OPTIONS = {"OK"};
  
  private final JOptionPane jop;
  
  private CancelAlert(String message) {
    jop = new JOptionPane(
      message,
      JOptionPane.ERROR_MESSAGE, 
      JOptionPane.OK_OPTION, 
      null, 
      OPTIONS, 
      OPTIONS[0]
    );
  }

  private boolean reveal(Image icon) {
    JDialog dialog = jop.createDialog("Atenção!");
    dialog.setAlwaysOnTop(true);
    dialog.setModal(true);
    dialog.setIconImage(icon);
    dialog.setVisible(true);
    dialog.dispose();
    visible.set(false);
    Object selectedValue = jop.getValue();
    return OPTIONS[0].equals(selectedValue);
  }
}
