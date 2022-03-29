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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

public final class CustomTooltipDelayer extends MouseAdapter {
  
  private static final int DEFAULT_DISMISS_TIMEOUT = ToolTipManager.sharedInstance().getDismissDelay();

  private final int delay;

  public static CustomTooltipDelayer attach(JComponent component, int delay) {
    CustomTooltipDelayer delayer = new CustomTooltipDelayer(delay);
    component.addMouseListener(delayer);
    return delayer;
  }

  public static CustomTooltipDelayer attach(JComponent component, float ratio) {
    return attach(component, (int)(DEFAULT_DISMISS_TIMEOUT * ratio) );
  }

  private CustomTooltipDelayer(int delay) {
    this.delay = delay;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    ToolTipManager.sharedInstance().setDismissDelay(this.delay);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    ToolTipManager.sharedInstance().setDismissDelay(DEFAULT_DISMISS_TIMEOUT);
  }
}
