package net.sf.anathema.character.alchemical.virtueflaw;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.anathema.character.library.virtueflaw.model.IVirtueFlaw;
import net.sf.anathema.character.library.virtueflaw.presenter.IVirtueFlawModel;
import net.sf.anathema.character.library.virtueflaw.presenter.IVirtueFlawView;
import net.sf.anathema.character.library.virtueflaw.presenter.VirtueFlawPresenter;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.workflow.textualdescription.ITextView;

public class AlchemicalVirtueFlawPresenter extends VirtueFlawPresenter {

  public AlchemicalVirtueFlawPresenter(IResources resources, IVirtueFlawView virtueFlawView, IVirtueFlawModel model) {
    super(resources, virtueFlawView, model);
  }

  @Override
  protected ITextView initNamePresentation(final IVirtueFlaw virtueFlaw) {
    return new ITextView() {
      
      @Override
      public void addTextChangedListener(IObjectValueChangedListener<String> listener) {
        //nothing to do;
      }

      @Override
      public void setEnabled(boolean enabled) {
        //nothing to do;
      }

      @Override
      public void setText(String text) {
        //nothing to do;
      }

      @Override
      public JComponent getComponent() {
        return new JPanel();
      }
    };
  }
}