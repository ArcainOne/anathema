package net.sf.anathema.character.equipment.impl;

import com.db4o.ext.DatabaseFileLockedException;
import net.sf.anathema.character.equipment.IEquipmentAdditionalModelTemplate;
import net.sf.anathema.character.equipment.impl.character.EquipmentAdditionalModelFactory;
import net.sf.anathema.character.equipment.impl.character.EquipmentAdditionalPersisterFactory;
import net.sf.anathema.character.equipment.impl.character.EquipmentAdditionalViewFactory;
import net.sf.anathema.character.equipment.impl.character.model.EquipmentAdditionalModelTemplate;
import net.sf.anathema.character.equipment.impl.item.model.db4o.Db4OEquipmentDatabase;
import net.sf.anathema.character.equipment.impl.reporting.content.*;
import net.sf.anathema.character.equipment.impl.reporting.rendering.arsenal.ArsenalEncoderFactory;
import net.sf.anathema.character.equipment.impl.reporting.rendering.panoply.ArmourEncoder;
import net.sf.anathema.character.equipment.impl.reporting.rendering.panoply.ArmourTableEncoder;
import net.sf.anathema.character.equipment.impl.reporting.rendering.panoply.PanoplyEncoderFactory;
import net.sf.anathema.character.equipment.impl.reporting.rendering.possessions.PossessionsEncoder;
import net.sf.anathema.character.equipment.impl.reporting.rendering.possessions.PossessionsEncoderFactory;
import net.sf.anathema.character.equipment.item.model.IEquipmentTemplateProvider;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.module.CharacterModule;
import net.sf.anathema.character.generic.framework.module.NullObjectCharacterModuleAdapter;
import net.sf.anathema.character.reporting.CharacterReportingModule;
import net.sf.anathema.character.reporting.CharacterReportingModuleObject;
import net.sf.anathema.character.reporting.pdf.content.ReportContentRegistry;
import net.sf.anathema.character.reporting.pdf.layout.extended.IEncodingRegistry;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.EncoderRegistry;
import net.sf.anathema.initialization.InitializationException;
import net.sf.anathema.lib.resources.IResources;

import java.io.File;

@CharacterModule
public class EquipmentCharacterModule extends NullObjectCharacterModuleAdapter {

  @Override
  public void addAdditionalTemplateData(ICharacterGenerics characterGenerics) throws InitializationException {
    File dataBaseFile = new File(characterGenerics.getDataFileProvider().getDataBaseDirectory(Db4OEquipmentDatabase.DATABASE_FOLDER), Db4OEquipmentDatabase.DATABASE_FILE);
    IEquipmentTemplateProvider equipmentDatabase;
    try {
      equipmentDatabase = new Db4OEquipmentDatabase(dataBaseFile);
    } catch (DatabaseFileLockedException e) {
      throw new InitializationException("Equipment database locked.\nAnathema may already be running.", e); //$NON-NLS-1$
    }
    characterGenerics.getAdditionalModelFactoryRegistry().register(IEquipmentAdditionalModelTemplate.ID, new EquipmentAdditionalModelFactory(equipmentDatabase));
    characterGenerics.getAdditonalPersisterFactoryRegistry().register(IEquipmentAdditionalModelTemplate.ID, new EquipmentAdditionalPersisterFactory());
    characterGenerics.getAdditionalViewFactoryRegistry().register(IEquipmentAdditionalModelTemplate.ID, new EquipmentAdditionalViewFactory());
    characterGenerics.getGlobalAdditionalTemplateRegistry().add(new EquipmentAdditionalModelTemplate());
  }

  @Override
  public void addReportTemplates(ICharacterGenerics generics, IResources resources) {
    CharacterReportingModuleObject moduleObject = generics.getModuleObjectMap().getModuleObject(CharacterReportingModule.class);
    registerEncoders(resources, moduleObject.getExtendedEncodingRegistry());
    registerContent(moduleObject.getContentRegistry(), resources);
    registerBoxEncoders(moduleObject.getEncoderRegistry());
  }

  private void registerBoxEncoders(EncoderRegistry registry) {
    registry.add(new ArsenalEncoderFactory());
    registry.add(new PanoplyEncoderFactory());
    registry.add(new PossessionsEncoderFactory());
  }

  private void registerContent(ReportContentRegistry registry, IResources resources) {
    registry.addFactory(Weaponry1stEditionContent.class, new Weaponry1stEditionContentFactory(resources));
    registry.addFactory(Weaponry2ndEditionContent.class, new Weaponry2ndEditionContentFactory(resources));
    registry.addFactory(ShieldContent.class, new ShieldContentFactory(resources));
    registry.addFactory(ArmourContent.class, new ArmourContentFactory(resources));
    registry.addFactory(WeaponryContent.class, new WeaponryContentFactory(resources));
    registry.addFactory(PossessionsContent.class, new PossessionsContentFactory(resources));
  }

  private void registerEncoders(IResources resources, IEncodingRegistry registry) {
    registry.setArmourContentEncoder(new ArmourEncoder(resources, new ArmourTableEncoder(ArmourContent.class)));
    registry.setPossessionsEncoder(new PossessionsEncoder());
  }
}
