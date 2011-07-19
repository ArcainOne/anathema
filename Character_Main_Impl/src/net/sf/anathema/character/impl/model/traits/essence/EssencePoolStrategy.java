package net.sf.anathema.character.impl.model.traits.essence;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.alchemical.slots.model.ICharmSlotsModel;
import net.sf.anathema.character.equipment.IEquipmentAdditionalModelTemplate;
import net.sf.anathema.character.equipment.character.model.IEquipmentAdditionalModel;
import net.sf.anathema.character.generic.additionalrules.IAdditionalEssencePool;
import net.sf.anathema.character.generic.additionalrules.IAdditionalRules;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.character.IMagicCollection;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.GlobalCharacterChangeAdapter;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IExtendedCharmData;
import net.sf.anathema.character.generic.template.essence.FactorizedTrait;
import net.sf.anathema.character.generic.template.essence.FactorizedTraitSumCalculator;
import net.sf.anathema.character.generic.template.essence.IEssenceTemplate;
import net.sf.anathema.character.generic.template.magic.IGenericCharmConfiguration;
import net.sf.anathema.character.generic.traits.IGenericTrait;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.generic.traits.types.VirtueType;
import net.sf.anathema.character.model.traits.essence.IEssencePoolStrategy;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.util.IdentifiedInteger;

public class EssencePoolStrategy implements IEssencePoolStrategy {

  private final ChangeControl control = new ChangeControl();
  private final IEssenceTemplate essenceTemplate;
  private final IAdditionalRules additionalRules;
  private final IGenericTraitCollection traitCollection;
  private final IMagicCollection magicCollection;
  private final IGenericCharmConfiguration charmConfiguration;
  private final ICharacterModelContext context;
  private IEquipmentAdditionalModel equipmentModel;

  public EssencePoolStrategy(IEssenceTemplate essenceTemplate,
                             ICharacterModelContext context,
                             IGenericTraitCollection traitCollection,
                             IMagicCollection magicCollection,
                             IGenericCharmConfiguration charmConfiguration,
                             IAdditionalRules additionalRules) {
    this.traitCollection = traitCollection;
    this.magicCollection = magicCollection;
    this.charmConfiguration = charmConfiguration;
    this.additionalRules = additionalRules;
    context.getCharacterListening().addChangeListener(new GlobalCharacterChangeAdapter() {
                                                        @Override
                                                        public void characterChanged() {
                                                          firePoolsChanged();
                                                        }
                                                      });
    this.context = context;
    this.essenceTemplate = essenceTemplate;
  }

  private void firePoolsChanged() {
    control.fireChangedEvent();
  }

  public void addPoolChangeListener(IChangeListener listener) {
    control.addChangeListener(listener);
  }

  public int getFullPersonalPool() {
    int additionalPool = 0;
    for (IAdditionalEssencePool pool : additionalRules.getAdditionalEssencePools()) {
      additionalPool += pool.getAdditionalPersonalPool(traitCollection,
                                                       magicCollection);
    }
    return getUnmodifiedPersonalPool() + additionalPool;
  }

  public int getExtendedPersonalPool() {
    int additionalPool = 0;
    for (IAdditionalEssencePool pool : additionalRules.getAdditionalEssencePools()) {
      additionalPool += pool.getAdditionalPersonalPool(traitCollection,
                                                       magicCollection);
    }
    return getStandardPersonalPool() + additionalPool;
  }

  public int getStandardPersonalPool() {
    int personal = getUnmodifiedPersonalPool();
    return personal
           - Math.max(0, getAttunementExpenditures()
                         - getUnmodifiedPeripheralPool())
           - getCharmAttunementExpenditures();
  }

  public int getUnmodifiedPersonalPool() {
    return getPool(essenceTemplate.getPersonalTraits(getWillpower(),
                                                     getVirtues(), getEssence()));
  }

  public int getFullPeripheralPool() {
    int additionalPool = 0;
    for (IAdditionalEssencePool pool : additionalRules.getAdditionalEssencePools()) {
      additionalPool += pool.getAdditionalPeripheralPool(traitCollection,
                                                         magicCollection);
    }
    return getUnmodifiedPeripheralPool() + additionalPool;
  }

  public int getExtendedPeripheralPool() {
    int additionalPool = 0;
    for (IAdditionalEssencePool pool : additionalRules.getAdditionalEssencePools()) {
      additionalPool += pool.getAdditionalPeripheralPool(traitCollection,
                                                         magicCollection);
    }
    return getStandardPeripheralPool() + additionalPool;
  }

  public int getStandardPeripheralPool() {
    int peripheral = getUnmodifiedPeripheralPool();
    return Math.max(0, peripheral - getAttunementExpenditures());
  }

  public int getUnmodifiedPeripheralPool() {
    return getPool(essenceTemplate.getPeripheralTraits(getWillpower(),
                                                       getVirtues(),
                                                       getEssence()));
  }
  
  public int getOverdrivePool() {
    int overdrive = 0;
    for (ICharm charm : charmConfiguration.getLearnedCharms()) {
      if (charm.hasAttribute(IExtendedCharmData.OVERDRIVE_ATTRIBUTE)) {
        int pool = 10;
        String value = charm.getAttributeValue(IExtendedCharmData.OVERDRIVE_ATTRIBUTE);
        if (value != null) {
          try {
            pool = Integer.valueOf(value);
          }
          catch (NumberFormatException e) {
            System.err.println("WARNING: could not parse Overdrive value for charm " + charm.getId() + "; ignoring keyword");
            continue;
          }
        }
        overdrive += pool;
        if (overdrive >= 25) {
          return 25;
        }
      }
    }
    return overdrive;
  }
  
  public IdentifiedInteger[] getComplexPools() {
    Map<String, Integer> complexPools = new HashMap<String, Integer>();
    for (IAdditionalEssencePool pool : additionalRules.getAdditionalEssencePools()) {
      for (IdentifiedInteger complexPool : pool.getAdditionalComplexPools(traitCollection, magicCollection)) {
        String id = complexPool.getId();
        int value = complexPool.getValue();
        if (complexPools.containsKey(id)) {
          value += complexPools.get(id);
        }
        complexPools.put(id, value);
      }
    }
    IdentifiedInteger[] r = new IdentifiedInteger[complexPools.size()];
    int i = 0;
    for (Entry<String, Integer> entry : complexPools.entrySet()) {
      r[i] = new IdentifiedInteger(entry.getKey(), entry.getValue());
      i++;
    }
    return r;
  }

  public int getAttunementExpenditures() {
    equipmentModel = (IEquipmentAdditionalModel) context.getAdditionalModel(IEquipmentAdditionalModelTemplate.ID);
    return equipmentModel == null ? 0 : equipmentModel.getTotalAttunementCost();
  }
  
  public int getCharmAttunementExpenditures()
  {
	ICharmSlotsModel model = (ICharmSlotsModel) context.getAdditionalModel(CharmSlotsTemplate.TEMPLATE_ID);
	return model == null ? 0 : model.getAttunedMotes();
  }

  private IGenericTrait[] getVirtues() {
    return new IGenericTrait[] {traitCollection.getTrait(VirtueType.Compassion),
                                traitCollection.getTrait(VirtueType.Conviction),
                                traitCollection.getTrait(VirtueType.Temperance),
                                traitCollection.getTrait(VirtueType.Valor)};
  }

  private IGenericTrait getWillpower() {
    return traitCollection.getTrait(OtherTraitType.Willpower);
  }

  private IGenericTrait getEssence() {
    return traitCollection.getTrait(OtherTraitType.Essence);
  }

  private int getPool(FactorizedTrait[] factorizedTraits) {
    return new FactorizedTraitSumCalculator().calculateSum(factorizedTraits);
  }
}