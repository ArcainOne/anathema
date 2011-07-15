package net.sf.anathema.character.equipment.impl.character.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.anathema.character.equipment.MagicalMaterial;
import net.sf.anathema.character.equipment.character.model.IEquipmentItem;
import net.sf.anathema.character.equipment.item.model.IEquipmentTemplateProvider;
import net.sf.anathema.character.equipment.template.IEquipmentTemplate;
import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.equipment.ArtifactAttuneType;
import net.sf.anathema.character.generic.equipment.weapon.IArmourStats;
import net.sf.anathema.character.generic.impl.rules.ExaltedRuleSet;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;

import com.db4o.query.Predicate;

public class EquipmentAdditionalModel extends AbstractEquipmentAdditionalModel {
  private final IEquipmentTemplateProvider equipmentTemplateProvider;
  private final MagicalMaterial defaultMaterial;
  private final IBasicCharacterData context;
  private final List<IEquipmentItem> naturalWeaponItems = new ArrayList<IEquipmentItem>();

  public EquipmentAdditionalModel(
	  IBasicCharacterData context,
      IArmourStats naturalArmour,
      IEquipmentTemplateProvider equipmentTemplateProvider,
      IEquipmentTemplate... naturalWeapons) {
    super(context.getRuleSet(), naturalArmour);
    this.context = context;
    this.defaultMaterial = evaluateDefaultMaterial();
    this.equipmentTemplateProvider = equipmentTemplateProvider;
    for (IEquipmentTemplate template : naturalWeapons) {
      if (template == null) {
        continue;
      }
      final IEquipmentItem item = new EquipmentItem(template, context.getRuleSet(), null);
      naturalWeaponItems.add(initItem(item));
    }
  }
  
  private MagicalMaterial evaluateDefaultMaterial() {
	    MagicalMaterial defaultMaterial = MagicalMaterial.getDefault(context.getCharacterType(), context.getCasteType());
	    if (defaultMaterial == null) {
	      return MagicalMaterial.Orichalcum;
	    }
	    return defaultMaterial;
	  }

  public IEquipmentItem[] getNaturalWeapons() {
    return naturalWeaponItems.toArray(new IEquipmentItem[naturalWeaponItems.size()]);
  }

  public boolean canBeRemoved(IEquipmentItem item) {
    return !naturalWeaponItems.contains(item);
  }

  public String[] getAvailableTemplateIds() {
    final Set<String> idSet = new HashSet<String>();
    equipmentTemplateProvider.queryContainer(new Predicate<IEquipmentTemplate>() {
		private static final long serialVersionUID = 1L;

	@Override
      public boolean match(IEquipmentTemplate candidate) {
        if (candidate.getStats(getRuleSet()).length > 0) {
          idSet.add(candidate.getName());
        }
        else {
          for (IExaltedRuleSet rules : ExaltedRuleSet.values()) {
            if (candidate.getStats(rules).length > 0) {
              return false;
            }
          }
          idSet.add(candidate.getName());
        }
        return false;
      }
    });
    return idSet.toArray(new String[idSet.size()]);
  }

  @Override
  protected IEquipmentTemplate loadEquipmentTemplate(String templateId) {
    return equipmentTemplateProvider.loadTemplate(templateId);
  }

  @Override
  protected IEquipmentItem getSpecialManagedItem(String templateId) {
    for (IEquipmentItem item : naturalWeaponItems) {
      if (templateId.equals(item.getTemplateId())) {
        return item;
      }
    }
    return null;
  }

  public MagicalMaterial getDefaultMaterial() {
    return defaultMaterial;
  }
  
  public ArtifactAttuneType[] getAttuneTypes(IEquipmentItem item)
  {
	  MagicalMaterial material = item.getMaterial();
	  switch (item.getMaterialComposition())
	  {
	  default:
	  case None:
		  return null;
	  case Fixed:
	  case Variable:
		  return MagicalMaterial.getAttunementTypes(context.getCharacterType(), context.getCasteType(), material);
	  case Compound:
		  return new ArtifactAttuneType[]
		       { ArtifactAttuneType.Unattuned, ArtifactAttuneType.FullyAttuned };
	  }
  }
}