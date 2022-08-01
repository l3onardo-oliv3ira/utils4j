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


package com.github.utils4j.echo.imp;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.echo.IEcho;
import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Strings;

public class EchoPanel extends JPanel implements IEcho {
  
  private static final Dimension DEFAULT_SIZE = new Dimension(500, 680);

  private static final int MAX_ITEM_COUNT = 800;
  
  private final JTextArea textArea = new JTextArea();

  private final String headerItemFormat;

  private int itemCount = 0;

  public EchoPanel() {
    this("%s\n");
  }
  
  public EchoPanel(String headerItemFormat) {  
    this.headerItemFormat = Args.requireNonNull(headerItemFormat, "headerItemFormat is null");
    setup();
  }

  protected JPanel north() {
    return new JPanel();
  }  

  @Override
  public JPanel asPanel() {
    return this;
  }

  @Override
  public final void clear() {
    textArea.setText(Strings.empty()); //auto clean
    itemCount = 0;
    onNewItem(Strings.empty(), itemCount);
  }

  @Override
  public final void addRequest(String request) {
    if (request != null) {
      addItem(request);
    }
  }
  
  private void addItem(String item) {
    if (itemCount >= MAX_ITEM_COUNT) {
      clear();      
    }
    onNewItem(item, ++itemCount);
    textArea.append(String.format(headerItemFormat, itemCount));
    textArea.append(item + "\n\r\n\r");
    textArea.getCaret().setDot(Integer.MAX_VALUE);
  }
  
  protected void onNewItem(String item, int count) {  
    
  }

  private void setup() {
    setBounds(getBounds().x, getBounds().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
    setupLayout();
  }
  
  private JScrollPane center() {
    JScrollPane centerPane = new JScrollPane();
    textArea.setRows(8);
    textArea.setEditable(false);
    centerPane.setViewportView(textArea);
    centerPane.setVisible(true);
    return centerPane;
  }

  private void setupLayout() {    
    setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    setLayout(new BorderLayout(0, 0));
    add(north(), BorderLayout.NORTH);
    add(center(), BorderLayout.CENTER);
  }
}
