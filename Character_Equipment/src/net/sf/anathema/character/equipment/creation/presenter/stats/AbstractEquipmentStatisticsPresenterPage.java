package net.sf.anathema.character.equipment.creation.presenter.stats;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.sf.anathema.character.equipment.creation.model.stats.IEquipmentStatisticsCreationModel;
import net.sf.anathema.character.equipment.creation.model.stats.IEquipmentStatisticsModel;
import net.sf.anathema.character.equipment.creation.presenter.stats.properties.AbstractEquipmentStatisticsProperties;
import net.sf.anathema.character.equipment.creation.view.IWeaponStatisticsView;
import net.sf.anathema.lib.gui.widgets.IntegerSpinner;
import net.sf.anathema.lib.gui.wizard.AbstractAnathemaWizardPage;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.workflow.intvalue.IIntValueModel;
import net.sf.anathema.lib.workflow.intvalue.IntValuePresentation;
import net.sf.anathema.lib.workflow.textualdescription.ITextView;
import net.sf.anathema.lib.workflow.textualdescription.ITextualDescription;
import net.sf.anathema.lib.workflow.textualdescription.TextualPresentation;

public abstract class AbstractEquipmentStatisticsPresenterPage<M extends IEquipmentStatisticsModel, P extends AbstractEquipmentStatisticsProperties> extends
    AbstractAnathemaWizardPage {

  private final P properties;
  private final M pageModel;
  private final IEquipmentStatisticsCreationModel overallModel;
  private final IEquipmentStatisticsCreationViewFactory viewFactory;
  private final IResources resources;
  private IWeaponStatisticsView view;

  public AbstractEquipmentStatisticsPresenterPage(
      IResources resources,
      P properties,
      IEquipmentStatisticsCreationModel overallModel,
      M pageModel,
      IEquipmentStatisticsCreationViewFactory viewFactory) {
    this.properties = properties;
    this.pageModel = pageModel;
    this.resources = resources;
    this.overallModel = overallModel;
    this.viewFactory = viewFactory;
    ITextualDescription name = getPageModel().getName();
    if (name.isEmpty()) {
      name.setText(properties.getDefaultName());
    }
  }

  protected final P getProperties() {
    return properties;
  }

  protected final M getPageModel() {
    return pageModel;
  }

  protected final IEquipmentStatisticsCreationViewFactory getViewFactory() {
    return viewFactory;
  }

  protected final IResources getResources() {
    return resources;
  }

  private boolean isNameDefined() {
    return !pageModel.getName().isEmpty();
  }

  public IBasicMessage getMessage() {
    if (!isNameDefined()) {
      return properties.getUndefinedNameMessage();
    }
    if (!isNameUnique()) {
      return properties.getDuplicateNameMessage();
    }
    return properties.getDefaultMessage();
  }

  private boolean isNameUnique() {
    return overallModel.isNameUnique(pageModel.getName().getText());
  }

  public final String getDescription() {
    return properties.getPageDescription();
  }

  public boolean canFinish() {
    return isNameDefined() && isNameUnique();
  }

  @Override
  protected final void initPageContent() {
    this.view = viewFactory.createEquipmentStatisticsView();
    initNameRow(getProperties().getNameLabel(), getPageModel().getName());
    addAdditionalContent();
  }

  private void initNameRow(String label, ITextualDescription textModel) {
    ITextView textView = view.addLineTextView(label);
    new TextualPresentation().initView(textView, textModel);
  }

  @Override
  protected final void addFollowUpPages(CheckInputListener inputListener) {
    if (!isTagsSupported()) {
      return;
    }
    addFollowupPage(new WeaponTagsPresenterPage(resources, overallModel, viewFactory), inputListener, new ICondition() {
      public boolean isFulfilled() {
        return isInLegalState();
      }
    });
  }

  protected boolean isInLegalState() {
    return canFinish();
  }

  protected abstract boolean isTagsSupported();

  protected abstract void addAdditionalContent();

  @Override
  protected final void initModelListening(CheckInputListener inputListener) {
    getPageModel().getName().addTextChangedListener(inputListener);
  }

  public final IWeaponStatisticsView getPageContent() {
    return view;
  }

  protected final void addLabelledComponentRow(final String[] labels, final Component[] contents) {
    Ensure.ensureArgumentTrue("Same number of labels required", labels.length == contents.length); //$NON-NLS-1$
    getPageContent().addDialogComponent(new IDialogComponent() {
      public void fillInto(JPanel panel, int columnCount) {
        for (int index = 0; index < contents.length; index++) {
          panel.add(new JLabel(labels[index]));
          panel.add(contents[index], GridDialogLayoutData.FILL_HORIZONTAL);
        }
      }

      public int getColumnCount() {
        return contents.length * 2;
      }
    });
  }

  protected final IntegerSpinner initIntegerSpinner(IIntValueModel intModel) {
    final IntegerSpinner spinner = new IntegerSpinner(intModel.getValue());
    new IntValuePresentation().initView(spinner, intModel);
    return spinner;
  }
}