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

package com.github.utils4j.echo;

import static com.github.utils4j.imp.Throwables.tryRun;

import java.awt.event.ActionEvent;

import com.github.utils4j.echo.imp.EchoNotifier;
import com.github.utils4j.gui.imp.SwingTools;
import com.github.utils4j.imp.Args;

public class EchoNotifierWindow extends EchoNotifier {
  
  private static final String DEFAULT_HEADER_FORMAT = 
      "==========================================================================\n" +
      " Recebida requisição %s: \n" +
      "==========================================================================\n";
  
  protected final String headerFormat;

  protected final String title;

  protected EchoFrame echoFrame;

  public EchoNotifierWindow(String title) {
    this(title, DEFAULT_HEADER_FORMAT);
  }
  
  public EchoNotifierWindow(String title, String headerFormat) {
    this.title = Args.requireNonNull(title, "title is null");
    this.headerFormat = Args.requireNonNull(headerFormat, "headerFormat is null");
  }

  @Override
  protected void doOpen() {
    super.doOpen();
    echoFrame = createFrame();
  }
  
  private final EchoFrame createFrame() {
    return new EchoFrame(getEcho(), title, createPanel()) {
      @Override
      protected void onEscPressed(ActionEvent e) {
        super.setVisible(false);
      }
    };
  }
  
  protected EchoPanel createPanel() {
    return new EchoPanel(headerFormat);
  }
  
  @Override
  protected void doClose() {
    super.doClose();
    tryRun(echoFrame::close);
  }

  @Override
  protected void display() {
    SwingTools.invokeAndWait(echoFrame::showToFront);
  }

  @Override
  protected boolean isDisplayed() {
    return echoFrame.isVisible();
  }
}
