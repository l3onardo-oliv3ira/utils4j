package com.github.utils4j.imp;

import com.github.utils4j.IRequestRejectNotifier;
import com.sun.net.httpserver.Filter;

@SuppressWarnings("restriction")
public abstract class RejectableFilter extends Filter {  //Filter deveria ser uma interface ao invés de uma classe abstrata (bad project)!

  protected final IRequestRejectNotifier notifier;  
  
  public RejectableFilter(IRequestRejectNotifier notifier) {
    this.notifier = Args.requireNonNull(notifier, "notifier is null");
  }
}  
