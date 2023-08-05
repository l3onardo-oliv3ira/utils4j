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

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.github.utils4j.imp.Strings;

public final class MessageAlert {

  private static final String[] OPTIONS = {"ENTENDI"};
  
  public static void showInfo(String message) {
    showInfo(message, null);
  }
  
  public static void showInfo(String message, Image icon) {
    invokeLater(() -> display(message, icon));
  }
  
  public static void showFail(String message) {
    showFail(message, null);
  }
  
  public static void showFail(String message, Image icon) {
    invokeLater(() -> displayFail(message, icon));
  }
  
  public static void showInfo(String message, String textButton, Image icon) {
    invokeLater(() -> display(message, textButton, icon));
  }
  
  public static boolean display(String message, Image icon) {
    return new MessageAlert(message, OPTIONS, JOptionPane.INFORMATION_MESSAGE).show(icon);
  }
  
  public static boolean display(String message, String textButton, Image icon) {
    return new MessageAlert(message, new String[] { textButton }, JOptionPane.INFORMATION_MESSAGE).show(icon);
  }

  public static boolean displayFail(String message, Image icon) {
    return new MessageAlert(message, OPTIONS, JOptionPane.ERROR_MESSAGE).show(icon);
  }
  
  public static boolean displayFail(String message, String textButton, Image icon) {
    return new MessageAlert(message, new String[] { textButton }, JOptionPane.ERROR_MESSAGE).show(icon);
  }
  
  public static boolean display(String message, String textButton, int optionPane, Image icon) {
    return new MessageAlert(message, new String[] { textButton }, optionPane).show(icon);
  }
  
  private final JOptionPane jop;
  
  private final String[] options;
  
  private MessageAlert(String message) {
    this(message, JOptionPane.OK_OPTION);
  }
  
  private MessageAlert(String message, int optionPane) {
    this(message, OPTIONS, optionPane);
  }

  private MessageAlert(String message, String[] options, int optionPane) {
    jop = new JOptionPane(
      Strings.trim(message),
      optionPane, 
      JOptionPane.OK_OPTION, 
      null, 
      this.options = options, 
      options[0]
    );
  }

  private boolean show(Image icon) {
    JDialog dialog = jop.createDialog("Atenção!");
    dialog.setAlwaysOnTop(true);
    dialog.setModal(true);
    dialog.setIconImage(icon);
    dialog.setVisible(true);
    dialog.dispose();
    Object selectedValue = jop.getValue();
    return options[0].equals(selectedValue);
  }
  
  public static void main(String[] args) {
    MessageAlert.showFail("leonardo", null);
  }
}
