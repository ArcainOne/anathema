package net.sf.anathema.cascades.module;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.sf.anathema.cascades.view.CascadeView;
import net.sf.anathema.framework.view.item.AbstractItemView;
import net.sf.anathema.platform.svgtree.presenter.view.ISvgTreeViewProperties;

public class CharmCascadeModuleView extends AbstractItemView implements ICascadeViewFactory {
  private final JPanel panel = new JPanel(new GridDialogLayout(1, false));

  public CharmCascadeModuleView(String printName, Icon icon) {
    super(printName, icon);
  }

  @Override
  public CascadeView createCascadeView(ISvgTreeViewProperties properties) {
    CascadeView view = new CascadeView(properties);
    panel.add(view.getComponent(), GridDialogLayoutData.FILL_BOTH);
    return view;
  }

  @Override
  public JComponent getComponent() {
    return panel;
  }
}