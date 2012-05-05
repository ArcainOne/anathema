package net.sf.anathema.platform.tree.view.visualizer;

import net.sf.anathema.platform.svgtree.document.visualizer.IVisualizedGraph;
import net.sf.anathema.platform.tree.view.container.AggregatedCascade;
import net.sf.anathema.platform.tree.view.container.DefaultContainerCascade;

import java.awt.Dimension;

public class SwingGraph implements IVisualizedGraph<AggregatedCascade> {
  private final DefaultContainerCascade container;
  private final Dimension dimension;

  public SwingGraph(DefaultContainerCascade container, Dimension dimension) {
    this.container = container;
    this.dimension = dimension;
  }


  @Override
  public Dimension getDimension() {
    return dimension;
  }

  @Override
  public boolean isSingleNode() {
    return false;
  }

  @Override
  public void translateBy(double x, double y) {
    container.moveBy(x, y);
  }

  @Override
  public void addTo(AggregatedCascade cascade) {
    cascade.add(container);
  }
}
