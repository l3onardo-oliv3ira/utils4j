package com.github.utils4j.gui.imp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

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