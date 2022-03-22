package com.github.utils4j.gui.imp;

import java.awt.GraphicsConfiguration;
import java.awt.Image;

public class SimpleFrame extends JEscFrame {
  private static final long serialVersionUID = 1L;

  public SimpleFrame(String title) {
    super(title);
  }

  public SimpleFrame(String title, Image icon) {
    super(title);
    super.setIconImage(icon);
  }

  public SimpleFrame(String title, Image icon, GraphicsConfiguration gc) {
    super(title, gc);
    super.setIconImage(icon);
  }
  
  public void showToFront(){
    SwingTools.showToFront(this);
  }  

  public void close() {
    this.setVisible(false);
    this.dispose();
  }
}
