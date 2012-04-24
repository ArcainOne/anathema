package net.sf.anathema.character.equipment.creation.presenter.stats;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.IPageContent;
import net.sf.anathema.character.equipment.creation.model.stats.IEquipmentStatisticsCreationModel;
import net.sf.anathema.character.equipment.creation.presenter.stats.properties.EquipmentTypeChoiceProperties;
import net.sf.anathema.character.equipment.creation.view.IEquipmentTypeChoiceView;
import net.sf.anathema.character.equipment.item.model.EquipmentStatisticsType;
import net.sf.anathema.lib.gui.wizard.AbstractAnathemaWizardPage;
import net.sf.anathema.lib.gui.wizard.IAnathemaWizardPage;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;
import net.sf.anathema.lib.resources.IResources;

import javax.swing.Action;
import java.awt.Component;

public class EquipmentTypeChoicePresenterPage extends AbstractAnathemaWizardPage {

  private final EquipmentTypeChoiceProperties properties;
  private final BasicMessage defaultMessage;
  private final IResources resources;
  private final IEquipmentStatisticsCreationModel model;
  private final IEquipmentStatisticsCreationViewFactory viewFactory;
  private IEquipmentTypeChoiceView view;

  public EquipmentTypeChoicePresenterPage(IResources resources, IEquipmentStatisticsCreationModel model,
                                          IEquipmentStatisticsCreationViewFactory viewFactory) {
    this.properties = new EquipmentTypeChoiceProperties(resources);
    this.defaultMessage = new BasicMessage(properties.getTypeChoiceMessage());
    this.resources = resources;
    this.model = model;
    this.viewFactory = viewFactory;
  }

  @Override
  public boolean canFinish() {
    return false;
  }

  @Override
  protected void initModelListening(CheckInputListener inputListener) {
    model.addEquipmentTypeChangeListener(inputListener);
  }

  @Override
  protected void addFollowUpPages(CheckInputListener inputListener) {
    addPage(EquipmentStatisticsType.CloseCombat, new CloseCombatStatisticsPresenterPage(resources, model, viewFactory),
            inputListener);
    addPage(EquipmentStatisticsType.RangedCombat,
            new RangedCombatStatisticsPresenterPage(resources, model, viewFactory), inputListener);
    addPage(EquipmentStatisticsType.Armor, new ArmourStatisticsPresenterPage(resources, model, viewFactory),
            inputListener);
    addPage(EquipmentStatisticsType.Artifact, new ArtifactStatisticsPresenterPage(resources, model, viewFactory),
            inputListener);
    addPage(EquipmentStatisticsType.TraitModifying,
            new TraitModifyingStatisticsPresenterPage(resources, model, viewFactory), inputListener);
  }

  @Override
  protected void initPageContent() {
    this.view = viewFactory.createTypeChoiceView();
    String label = properties.getOffensiveLabel();
    addStatisticsTypeRow(label, EquipmentStatisticsType.CloseCombat);
    addStatisticsTypeRow("", EquipmentStatisticsType.RangedCombat); //$NON-NLS-1$
    view.addHorizontalLine();
    addStatisticsTypeRow(properties.getDefensiveLabel(), EquipmentStatisticsType.Armor);
    view.addHorizontalLine();
    addStatisticsTypeRow(properties.getOtherLabel(), EquipmentStatisticsType.TraitModifying);
    addStatisticsTypeRow("", EquipmentStatisticsType.Artifact);
  }

  private void addPage(final EquipmentStatisticsType type, IAnathemaWizardPage page, CheckInputListener inputListener) {
    addFollowupPage(page, inputListener, new ICondition() {
      @Override
      public boolean isFulfilled() {
        return model.isEquipmentTypeSelected(type);
      }
    });
  }

  private void addStatisticsTypeRow(String label, final EquipmentStatisticsType type) {
    Action action = new SmartAction(properties.getIcon(type)) {

      @Override
      protected void execute(Component parentComponent) {
        model.setEquipmentType(type);
      }
    };
    String typeLabel = properties.getLabel(type);
    view.addStatisticsRow(label, action, typeLabel, model.getEquipmentType() == type);
  }

  @Override
  public IPageContent getPageContent() {
    return view;
  }

  @Override
  public IBasicMessage getMessage() {
    return defaultMessage;
  }

  @Override
  public String getDescription() {
    return properties.getTypeChoiceTitle();
  }
}