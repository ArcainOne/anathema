package net.sf.anathema.character.presenter;

import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.library.trait.IModifiableCapTrait;
import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.character.library.trait.presenter.TraitPresenter;
import net.sf.anathema.character.model.traits.ICoreTraitConfiguration;
import net.sf.anathema.character.model.traits.essence.IEssencePoolConfiguration;
import net.sf.anathema.character.view.IBasicAdvantageView;
import net.sf.anathema.framework.value.IIntValueView;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.gui.IPresenter;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.workflow.labelledvalue.IValueView;

public class EssenceConfigurationPresenter implements IPresenter {

  private final IBasicAdvantageView view;
  private final IEssencePoolConfiguration essence;
  private final IResources resources;
  private final ICoreTraitConfiguration traitConfiguration;

  public EssenceConfigurationPresenter(
      IResources resources,
      final IEssencePoolConfiguration essence,
      final ICoreTraitConfiguration traitConfiguration,
      final IBasicAdvantageView view) {
    this.resources = resources;
    this.essence = essence;
    this.traitConfiguration = traitConfiguration;
    this.view = view;
  }

  public void initPresentation() {
    ITrait essenceTrait = traitConfiguration.getTrait(OtherTraitType.Essence);
    IIntValueView essenceView = view.addEssenceView(resources.getString("Essence.Name"), //$NON-NLS-1$
        essenceTrait.getCurrentValue(),
        essenceTrait.getMaximalValue(),
        (IModifiableCapTrait) essenceTrait);
    if (essence.isEssenceUser()) {
      final IValueView<String> personalView = view.addPoolView(
          resources.getString("EssencePool.Name.Personal"), essence.getPersonalPool()); //$NON-NLS-1$
      
      if (essence.hasPeripheralPool()) {
        final IValueView<String> peripheralView = view.addPoolView(
            resources.getString("EssencePool.Name.Peripheral"), essence.getPeripheralPool()); //$NON-NLS-1$
        final IValueView<String> attunementView = view.addPoolView(
                resources.getString("EssencePool.Name.Attunement"), essence.getAttunedPool());
        essence.addPoolChangeListener(new IChangeListener() {
          public void changeOccured() {
            personalView.setValue(essence.getPersonalPool());
            peripheralView.setValue(essence.getPeripheralPool());
            attunementView.setValue(essence.getAttunedPool());
          }
        });
      }
      else {
    	final IValueView<String> attunementView = view.addPoolView(
    	          resources.getString("EssencePool.Name.Attunement"), essence.getAttunedPool());
        essence.addPoolChangeListener(new IChangeListener() {
          public void changeOccured() {
            personalView.setValue(essence.getPersonalPool());
            attunementView.setValue(essence.getAttunedPool());
          }
        });
      }
    }
    new TraitPresenter(essenceTrait, essenceView).initPresentation();
  }
}