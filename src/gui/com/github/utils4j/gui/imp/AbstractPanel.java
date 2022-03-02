package com.github.utils4j.gui.imp;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.utils4j.imp.Strings;

public abstract class AbstractPanel extends JPanel {

  private final String baseIcons;
  
  public AbstractPanel() {
    this("/icons/buttons/");
  }
  
  public AbstractPanel(String baseIcons) {
    this.baseIcons = Strings.text(baseIcons, "/");
  }

  protected class BigButton extends JButton {
    public BigButton() {
      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      setHideActionText(true);
    }
  }

  protected class StandardButton extends JButton {
    public StandardButton() {
      setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
      setHideActionText(true);
    }
  }

  protected Icon newIcon(String name) {
    URL location = getClass().getResource(baseIcons + name + ".png");
    return location != null ? new ImageIcon(location) : null;
  }
}
