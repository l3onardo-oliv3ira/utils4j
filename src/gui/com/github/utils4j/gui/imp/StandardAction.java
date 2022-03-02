package com.github.utils4j.gui.imp;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.github.utils4j.gui.IResourceAction;

public abstract class StandardAction extends AbstractAction {

  public StandardAction(IResourceAction resource) {
    putValue(Action.NAME, resource.name());
    putValue(Action.MNEMONIC_KEY, resource.mnemonic());
    putValue(Action.ACCELERATOR_KEY, resource.shortcut());
    putValue(Action.SHORT_DESCRIPTION, resource.tooltip());
    putValue(Action.SMALL_ICON, resource.menuIcon());
    putValue(Action.LARGE_ICON_KEY, resource.buttonIcon());
  }

  public StandardAction(String name) {
    putValue(Action.NAME, name);
  }

  public final void select(boolean select) {
    putValue(Action.SELECTED_KEY, select);
  }
}
