package net.sf.anathema.charmentry.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import net.sf.anathema.charmentry.module.ICharmEntryViewFactory;
import net.sf.anathema.charmentry.presenter.model.ICharmEntryModel;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;
import net.sf.anathema.lib.resources.IResources;

public class SecondEditionPrerequisiteCharmsPage extends PrerequisiteCharmsPage {

  public SecondEditionPrerequisiteCharmsPage(
      IResources resources,
      ICharmEntryModel model,
      ICharmEntryViewFactory viewFactory) {
    super(resources, model, viewFactory);
  }

  @Override
  protected void addFollowUpPages(CheckInputListener inputListener) {
    super.addFollowUpPages(inputListener);
    addFollowupPage(
        new KeywordEntryPage(getResources(), getModel(), getViewFactory()),
        inputListener,
        new ICondition() {
          public boolean isFulfilled() {
            return true;
          }
        });

  }

  @Override
  protected void initPageContent() {
    super.initPageContent();
    final JToggleButton button = getPageContent().addToggleButton(getProperties().getExcellencyString());
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getPageModel().setRequiresExcellency(button.isSelected());
      }
    });
  }
}