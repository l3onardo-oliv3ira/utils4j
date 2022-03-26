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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class JEscDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  public JEscDialog() {
    this((Frame)null, false);
  }

  public JEscDialog(Frame owner) {
    this(owner, false);
  }

  public JEscDialog(Frame owner, boolean modal) {
    this(owner, (String)null, modal);
  }

  public JEscDialog(Frame owner, String title) {
    this(owner, title, false);
  }

  public JEscDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
  }

  public JEscDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
    super(owner, title, modal, gc);
  }

  public JEscDialog(Dialog owner) {
    this(owner, false);
  }

  public JEscDialog(Dialog owner, boolean modal) {
    this(owner, (String)null, modal);
  }

  public JEscDialog(Dialog owner, String title) {
    this(owner, title, false);
  }

  public JEscDialog(Dialog owner, String title, boolean modal) {
    super(owner, title, modal);
  }

  public JEscDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
    super(owner, title, modal, gc);
  }

  public JEscDialog(Window owner, Dialog.ModalityType modalityType) {
    this(owner, "", modalityType);
  }

  public JEscDialog(Window owner, String title, Dialog.ModalityType modalityType) {
    super(owner, title, modalityType);
  }

  public JEscDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
    super(owner, title, modalityType, gc);
  }

  @Override
  protected JRootPane createRootPane() {
    JRootPane rootPane = new JRootPane();
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
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
    JEscDialog.this.setVisible(false);
    JEscDialog.this.dispose();
  }
}
