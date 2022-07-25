package com.github.utils4j.gui.imp;

import com.github.utils4j.IEchoNotifier;
import com.github.utils4j.imp.Args;

public class EchoNotifierWrapper implements IEchoNotifier {

  private final IEchoNotifier notifier;
  
  protected EchoNotifierWrapper(IEchoNotifier notifier) {
    this.notifier = Args.requireNonNull(notifier, "notifier is null");
  }

  @Override
  public void accept(String t) {
    notifier.accept(t);
  }

  @Override
  public void open() {
    notifier.open();
  }

  @Override
  public boolean isOpen() {
    return notifier.isOpen();
  }

  @Override
  public void close() {
    notifier.close();
  }

  @Override
  public boolean isVisible() {
    return notifier.isVisible();
  }

  @Override
  public void show() {
    notifier.show();
  }
}
