package com.github.utils4j.gui;

import javax.swing.Icon;
import javax.swing.KeyStroke;

public interface IResourceAction {

  String name();

  Integer mnemonic();

  KeyStroke shortcut();

  String tooltip();

  Icon menuIcon();

  Icon buttonIcon();

}