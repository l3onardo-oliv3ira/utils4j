package com.github.utils4j.gui.imp;

import com.github.utils4j.IEchoNotifier;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class EchoNotifier implements IEchoNotifier {

  private BehaviorSubject<String> echo;

  @Override
  public final boolean isOpen() {
    return echo != null;
  }

  @Override
  public final void open() {
    if (!isOpen()) {
      doOpen();
    }
  }
  
  protected final Observable<String> getEcho() {
    return echo;
  }

  protected void doOpen() {
    this.echo = BehaviorSubject.create();
  }
  
  @Override
  public final void accept(String message) {
    show();
    echo.onNext(message);
  }

  @Override
  public final void close() {
    if (isOpen()) {
      doClose();
    }
  }

  protected void doClose() {
    if (echo != null) {
      echo.onComplete();
      echo = null;
    }
  }

  @Override
  public final boolean isVisible() {
    return isOpen() && isDisplayed();
  }

  @Override
  public final void show() {
    open();
    if (!isVisible()) {
      display();
    }
  }
  
  protected abstract void display();

  protected abstract boolean isDisplayed();

}
