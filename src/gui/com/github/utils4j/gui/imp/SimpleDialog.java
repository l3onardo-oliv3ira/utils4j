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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;

public class SimpleDialog extends JEscDialog {
  
  private static final long serialVersionUID = 1L;
  
  public SimpleDialog(String title) {
    super((Frame)null, title);
  }
  
  public SimpleDialog(String title, Image icon) {
    this((Frame)null, title, icon);
  }
  
  public SimpleDialog(String title, Image icon, boolean modal) {
    this((Frame)null, title, icon, modal);
  }
  
  public SimpleDialog(Frame owner, String title, Image icon) {
    this(owner, title, icon, false);
  }

  public SimpleDialog(Frame owner, String title, Image icon, boolean modal) {
    super(owner, title, modal);
    this.setIconImage(icon);
  }

  protected void setFixedMinimumSize(Dimension dimension) {
    SwingTools.setFixedMinimumSize(this, dimension);
  }
  
  protected final void toCenter() {
    super.setLocationRelativeTo(null);
  }

  public void showToFront(){
    SwingTools.showToFront(this);
  }  
  
  public void close() {
    this.setVisible(false);
    this.dispose();
  }
}
