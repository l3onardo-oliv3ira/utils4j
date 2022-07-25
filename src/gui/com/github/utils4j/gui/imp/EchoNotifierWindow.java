package com.github.utils4j.gui.imp;

import static com.github.utils4j.imp.Throwables.tryRun;

import java.awt.event.ActionEvent;

import com.github.utils4j.IEchoNotifier;
import com.github.utils4j.imp.Args;

public class EchoNotifierWindow extends EchoNotifier implements IEchoNotifier {
  
  protected final String headerFormat;

  protected final String title;

  protected EchoFrame echoFrame;

  public EchoNotifierWindow(String title, String headerFormat) {
    this.title = Args.requireNonNull(title, "title is null");
    this.headerFormat = Args.requireNonNull(headerFormat, "headerFormat is null");
  }

  @Override
  protected void doOpen() {
    super.doOpen();
    echoFrame = createFrame();
  }
  
  protected EchoFrame createFrame() {
    return new EchoFrame(title, headerFormat, getEcho()) {
      @Override
      protected void onEscPressed(ActionEvent e) {
        super.setVisible(false);
      }
    };
  }
  
  @Override
  protected void doClose() {
    super.doClose();
    tryRun(echoFrame::close);
  }

  @Override
  protected void display() {
    SwingTools.invokeAndWait(echoFrame::showToFront);
  }

  @Override
  protected boolean isDisplayed() {
    return echoFrame.isVisible();
  }
}
