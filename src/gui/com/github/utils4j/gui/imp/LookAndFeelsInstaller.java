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

  private LookAndFeelsInstaller() {}

  public static void install(String looksAndFeelsName, Component root) {
    Args.requireNonNull(looksAndFeelsName, "looksAndFeels is null");
    Args.requireNonNull(root, "root is null");
    
    LookAndFeel laf = getLookAndFeel();

    String currentLookAndFeels = laf == null ? "undefined" : laf.getName();
    
    if (currentLookAndFeels.equals(looksAndFeelsName))
      return;
    
    boolean installed = false;
    for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
      if (info.getName().equals(looksAndFeelsName)) {
        tryRuntime(() -> setLookAndFeel(info.getClassName())); 
        installed = true;
        break;
      }
    }
    if (!installed) {
      tryRuntime(() -> setLookAndFeel(getSystemLookAndFeelClassName())); 
    }
    
    updateComponentTreeUI(root);
  }
}
