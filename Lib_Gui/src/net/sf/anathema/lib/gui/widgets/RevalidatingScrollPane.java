package net.sf.anathema.lib.gui.widgets;

import java.awt.Component;

import javax.swing.JScrollPane;

import net.sf.anathema.lib.gui.GuiUtilities;

public class RevalidatingScrollPane extends JScrollPane {

  private static final long serialVersionUID = 3738627177957841545L;

  public RevalidatingScrollPane(Component view) {
    super(view);
  }

  public RevalidatingScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
    super(view, vsbPolicy, hsbPolicy);
  }

  @Override
  public void revalidate() {
    GuiUtilities.revalidateTree(this);
  }
}