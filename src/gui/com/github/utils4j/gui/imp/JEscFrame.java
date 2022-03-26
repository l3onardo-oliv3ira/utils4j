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

import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class JEscFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public JEscFrame() {}

  public JEscFrame(GraphicsConfiguration gc) {
    super(gc);
  }

  public JEscFrame(String title) {
    super(title);
  }

  public JEscFrame(String title, GraphicsConfiguration gc) {
    super(title, gc);
  }

  protected JRootPane createRootPane() {
    JRootPane rootPane = new JRootPane();
    KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
    rootPane.getInputMap(2).put(stroke, "escapeKey");
    rootPane.getActionMap().put("escapeKey", new AbstractAction() {
      private static final long serialVersionUID = 1L;
      public void actionPerformed(ActionEvent e) {
        onEscPressed(e);
      }
    });
    return rootPane;
  }
  
  protected void onEscPressed(ActionEvent e) {
    this.close();
  }

  protected void close() {
    this.setVisible(false);
    this.dispose();
  }
}
