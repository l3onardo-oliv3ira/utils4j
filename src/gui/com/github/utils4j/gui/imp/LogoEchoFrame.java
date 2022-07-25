package com.github.utils4j.gui.imp;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.reactivex.Observable;
import net.miginfocom.swing.MigLayout;

public abstract class LogoEchoFrame extends EchoFrame {

  private static final long serialVersionUID = 1L;

  public LogoEchoFrame(String title, String headerItemFormat, Observable<String> echoCallback) {
    super(title, headerItemFormat, echoCallback);
  }

  @Override
  protected JPanel north() {
    JPanel north = super.north();
    north.setLayout(new MigLayout("fill"));
    JLabel image = new JLabel();
    image.setIcon(getLogo());
    north.add("center", image);
    return north;
  }
  
  @Override
  protected void onEscPressed(ActionEvent e) {
    this.setVisible(false);
  }

  protected abstract Icon getLogo();
}
