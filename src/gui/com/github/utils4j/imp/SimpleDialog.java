package com.github.utils4j.imp;

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

  public void close() {
    this.setVisible(false);
    this.dispose();
  }
}
