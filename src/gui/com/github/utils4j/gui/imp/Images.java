package com.github.utils4j.gui.imp;

import com.github.utils4j.gui.IPicture;

public enum Images implements IPicture {
  FIRST("/file-first.png"),
  
  UP("/file-up.png"),

  DOWN("/file-down.png"),
  
  LAST("/file-last.png");

  final String path;
  
  Images(String path) {
    this.path = path;
  }

  @Override
  public String path() {
    return path;
  }
}
