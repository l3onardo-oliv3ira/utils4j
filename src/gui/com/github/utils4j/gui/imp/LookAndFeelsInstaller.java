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

import static com.github.utils4j.imp.Throwables.tryRuntime;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.getLookAndFeel;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;

import java.awt.Component;

import javax.swing.LookAndFeel;
import javax.swing.UIManager.LookAndFeelInfo;

import com.github.utils4j.imp.Args;

public final class LookAndFeelsInstaller {

  public static final String UNDEFINED = "undefined";
  
/*
Metal
CDE/Motif
Nimbus
Windows
Windows Classic
*/
  private LookAndFeelsInstaller() {}

  public static void install(String looksAndFeelsName) throws Exception { 
    install(looksAndFeelsName, null);
  }
  
  public static void install(String looksAndFeelsName, Component root) throws Exception {
    Args.requireNonNull(looksAndFeelsName, "looksAndFeels is null");
    
    LookAndFeel laf = getLookAndFeel();

    String currentLookAndFeels = laf == null ? UNDEFINED : laf.getName();
    
    if (currentLookAndFeels.equals(looksAndFeelsName))
      return;
    
    boolean installed = false;
    for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
      if (info.getName().equals(looksAndFeelsName)) {
        setLookAndFeel(info.getClassName()); 
        installed = true;
        break;
      }
    }
    if (!installed) {
      tryRuntime(() -> setLookAndFeel(getSystemLookAndFeelClassName())); 
    }
    
    if (root != null) {
      updateComponentTreeUI(root);
    }
  }
}
