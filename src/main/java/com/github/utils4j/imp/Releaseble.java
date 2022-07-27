package com.github.utils4j.imp;

import io.reactivex.disposables.Disposable;

public class Releaseble implements Disposable {
  
  private final Disposable token;
  
  public Releaseble(Disposable token) {
    this.token = Args.requireNonNull(token, "token is null");
  }
  
  @Override
  public void dispose() {
    token.dispose();
    onRelease(token.isDisposed());
  }

  protected void onRelease(boolean disposed) {    
  }

  @Override
  public boolean isDisposed() {
    return token.isDisposed();
  }
} 
