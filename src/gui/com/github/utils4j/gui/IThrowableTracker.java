package com.github.utils4j.gui;

import static com.github.utils4j.imp.Strings.emptyArray;;

public interface IThrowableTracker {
  static IThrowableTracker NOTHING = (t) -> emptyArray();
  
  static IThrowableTracker orNothing(IThrowableTracker t) {
    return t == null ? NOTHING : t;
  }
  
  String[] track(Throwable cause);
}
