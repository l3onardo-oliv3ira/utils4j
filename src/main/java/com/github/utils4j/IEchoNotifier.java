package com.github.utils4j;

import java.util.function.Consumer;

public interface IEchoNotifier extends Consumer<String> {
  boolean isOpen();
  boolean isVisible();

  void show();
  void open();
  void close();
}