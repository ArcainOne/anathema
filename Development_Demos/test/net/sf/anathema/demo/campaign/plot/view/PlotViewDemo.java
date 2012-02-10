package net.sf.anathema.demo.campaign.plot.view;

import de.jdemo.junit.DemoAsTestRunner;
import net.sf.anathema.campaign.concrete.plot.PlotModel;
import net.sf.anathema.campaign.presenter.plot.PlotPresenter;
import net.sf.anathema.campaign.presenter.view.plot.IPlotView;
import net.sf.anathema.campaign.view.plot.PlotView;
import net.sf.anathema.framework.resources.AnathemaResources;
import net.sf.anathema.lib.resources.IResources;
import de.jdemo.extensions.SwingDemoCase;
import org.junit.runner.RunWith;

@RunWith(DemoAsTestRunner.class)
public class PlotViewDemo extends SwingDemoCase {

  public void demo() {
    final IPlotView view = new PlotView();
    IResources resources = new AnathemaResources();
    new PlotPresenter(resources, view, new PlotModel()).initPresentation();
    show(view.getComponent());
  }
}