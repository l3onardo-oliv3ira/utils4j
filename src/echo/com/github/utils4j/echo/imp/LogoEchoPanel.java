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
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
    
public abstract class LogoEchoPanel extends EchoPanel {

  private static final long serialVersionUID = 1L;

  private JLabel header;
  
  public LogoEchoPanel() {
    super();
  }
  
  public LogoEchoPanel(String headerItemFormat) {
    super(headerItemFormat);
  }

  private JPanel middle() {
    JPanel center = new JPanel();
    center.add("center", new JLabel(getLogo()));
    return center;
  }
  
  private JPanel right() {
    header = new JLabel();
    header.setFont(new Font("Tahoma", Font.PLAIN, 22));    
    JPanel east = new JPanel();
    east.add("center", header);
    return east;
  }
  
  protected void onNewItem(String item, int count) {  
    header.setText("Requisição: " + count);  
  }
  
  @Override
  protected JPanel north() {
    JPanel north = super.north();   
    north.setLayout(new BorderLayout(0, 0));
    north.add(middle(), BorderLayout.CENTER);
    north.add(right(), BorderLayout.SOUTH);
    return north;
  }
  
  protected abstract Icon getLogo();
}
