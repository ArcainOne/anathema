package net.sf.anathema.character.equipment.creation.model.stats;

import net.sf.anathema.character.equipment.item.model.EquipmentStatisticsType;
import net.sf.anathema.lib.control.change.IChangeListener;

public interface IEquipmentStatisticsCreationModel {

  public void setEquipmentType(EquipmentStatisticsType type);

  public ICloseCombatStatsticsModel getCloseCombatStatsticsModel();

  public IRangedCombatStatisticsModel getRangedWeaponStatisticsModel();

  public IShieldStatisticsModel getShieldStatisticsModel();

  public IArmourStatisticsModel getArmourStatisticsModel();
  
  public IArtifactStatisticsModel getArtifactStatisticsModel();
  
  public ITraitModifyingStatisticsModel getTraitModifyingStatisticsModel();

  public void addEquipmentTypeChangeListener(IChangeListener changeListener);

  public boolean isEquipmentTypeSelected(EquipmentStatisticsType type);
  
  public IApplicableMaterialsModel getApplicableMaterialsModel();

  public IWeaponTagsModel getWeaponTagsModel();

  public EquipmentStatisticsType getEquipmentType();

  public boolean isNameUnique(String name);
}