package net.sf.anathema.charmentry.presenter;

import net.sf.anathema.character.generic.magic.charms.ICharmAttribute;
import net.sf.anathema.character.library.removableentry.presenter.IRemovableEntryListener;
import net.sf.anathema.character.library.removableentry.presenter.IRemovableEntryView;
import net.sf.anathema.charmentry.module.ICharmEntryViewFactory;
import net.sf.anathema.charmentry.presenter.model.ICharmEntryModel;
import net.sf.anathema.charmentry.presenter.model.IKeywordEntryModel;
import net.sf.anathema.charmentry.presenter.view.IKeywordView;
import net.sf.anathema.charmentry.properties.IKeywordEntryPageProperties;
import net.sf.anathema.charmentry.properties.KeywordEntryPageProperties;
import net.sf.anathema.framework.presenter.view.IButtonControlledObjectSelectionView;
import net.sf.anathema.lib.control.ObjectValueListener;
import net.sf.anathema.lib.gui.dialog.core.IPageContent;
import net.sf.anathema.lib.gui.wizard.AbstractAnathemaWizardPage;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.message.IBasicMessage;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identified;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class KeywordEntryPage extends AbstractAnathemaWizardPage {

  private final ICharmEntryModel model;
  private final ICharmEntryViewFactory viewFactory;
  private final IKeywordEntryPageProperties properties;
  private IKeywordView view;
  private final Map<ICharmAttribute, IRemovableEntryView> viewsByAttribute = new HashMap<ICharmAttribute, IRemovableEntryView>();

  public KeywordEntryPage(IResources resources, ICharmEntryModel model, ICharmEntryViewFactory viewFactory) {
    this.properties = new KeywordEntryPageProperties(resources);
    this.model = model;
    this.viewFactory = viewFactory;
  }

  @Override
  protected void addFollowUpPages(CheckInputListener inputListener) {
    // nothing to do
  }

  @Override
  protected void initModelListening(CheckInputListener inputListener) {
    // nothing to do
  }

  @Override
  protected void initPageContent() {
    this.view = viewFactory.createKeywordEntryView();
    final IButtonControlledObjectSelectionView<Identified> selectionView = view.addObjectSelectionView(
        properties.getKeywordRenderer(),
        properties.getKeywordLabel(),
        properties.getAddIcon());
    selectionView.setObjects(getPageModel().getAvailableKeywords());
    selectionView.addObjectSelectionChangedListener(new ObjectValueListener<Identified>() {
      @Override
      public void valueChanged(Identified newValue) {
        getPageModel().setCurrentKeyword(newValue);
      }
    });
    selectionView.addButtonListener(new ObjectValueListener<Identified>() {
      @Override
      public void valueChanged(Identified newValue) {
        getPageModel().commitSelection();
      }
    });
    getPageModel().addModelChangeListener(new IRemovableEntryListener<ICharmAttribute>() {
      @Override
      public void entryAdded(final ICharmAttribute entry) {
        IRemovableEntryView entryView = view.addEntryView(properties.getRemoveIcon(), null, entry.getId());
        viewsByAttribute.put(entry, entryView);
        entryView.addButtonListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            getPageModel().removeEntry(entry);
          }
        });
      }

      @Override
      public void entryAllowed(boolean complete) {
        selectionView.setButtonEnabled(complete);
      }

      @Override
      public void entryRemoved(ICharmAttribute entry) {
        view.removeEntryView(viewsByAttribute.get(entry));
        viewsByAttribute.remove(entry);
      }
    });
    selectionView.setButtonEnabled(false);
  }

  private IKeywordEntryModel getPageModel() {
    return model.getKeywordEntryModel();
  }

  @Override
  public boolean canFinish() {
    return true;
  }

  @Override
  public String getDescription() {
    return properties.getPageTitle();
  }

  @Override
  public IBasicMessage getMessage() {
    return properties.getDefaultMessage();
  }

  @Override
  public IPageContent getPageContent() {
    return view;
  }
}