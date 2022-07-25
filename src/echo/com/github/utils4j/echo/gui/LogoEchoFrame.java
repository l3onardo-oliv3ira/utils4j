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

package com.github.utils4j.echo.gui;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.reactivex.Observable;

public abstract class LogoEchoFrame extends EchoFrame {

  private static final long serialVersionUID = 1L;

  public LogoEchoFrame(Observable<String> echoCallback) {
    super(echoCallback);
  }
  
  public LogoEchoFrame(String title, String headerItemFormat, Observable<String> echoCallback) {
    super(title, headerItemFormat, echoCallback);
  }

  @Override
  protected JPanel north() {
    JPanel north = super.north();
    north.add("center", new JLabel(getLogo()));
    return north;
  }
  
  @Override
  protected void onEscPressed(ActionEvent e) {
    this.setVisible(false);
  }

  protected abstract Icon getLogo();
}
