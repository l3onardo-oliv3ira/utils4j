package com.github.utils4j.echo;

import javax.swing.JPanel;

public interface IEcho {

  void clear();

  void addRequest(String request);
  
  JPanel asPanel();
}