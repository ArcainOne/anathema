package net.sf.anathema.character.generic.framework;

import net.sf.anathema.character.generic.framework.module.CharacterModuleContainer;
import net.sf.anathema.character.generic.framework.module.ICharacterModule;
import net.sf.anathema.character.generic.framework.module.object.ICharacterModuleObject;
import net.sf.anathema.initialization.InitializationException;
import net.sf.anathema.initialization.repository.IDataFileProvider;
import net.sf.anathema.lib.logging.Logger;
import net.sf.anathema.lib.resources.IResources;

import java.util.ArrayList;
import java.util.List;

public class CharacterModuleContainerInitializer {

  private final Logger logger = Logger.getLogger(CharacterModuleContainerInitializer.class);

  @SuppressWarnings("serial")
  private final List<String> moduleNameList = new ArrayList<String>() {
    {
      add("net.sf.anathema.character.reporting.CharacterReportingModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.equipment.impl.EquipmentCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.meritsflaws.MeritsFlawsModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.mutations.MutationsModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.impl.specialties.SpecialtiesModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.craft.CraftModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.linguistics.LinguisticsModule"); //$NON-NLS-1$
      // Thaumaturgy removed pending a rewrite for the new character sheet
      // add("net.sf.anathema.character.thaumaturgy.ThaumaturgyModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.intimacies.IntimaciesModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.mortal.MortalCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.abyssal.AbyssalCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.abyssal.Abyssal2ndCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.db.DbCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.lunar.LunarCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.solar.SolarCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.spirit.SpiritCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.ghost.GhostCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.alchemical.AlchemicalCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.sidereal.SiderealCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.infernal.InfernalCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.godblooded.GodBloodedCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.martialarts.MartialArtsCharacterModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.reporting.first.FirstEditionReportingModule"); //$NON-NLS-1$
      add("net.sf.anathema.character.reporting.second.SecondEditionReportingModule"); //$NON-NLS-1$
    }
  };

  public CharacterModuleContainer initContainer(IResources resources, IDataFileProvider dataFileProvider) throws InitializationException {
    CharacterModuleContainer container = new CharacterModuleContainer(resources, dataFileProvider);
    for (String moduleName : moduleNameList) {
      addModule(container, moduleName);
    }
    return container;
  }

  private void addModule(CharacterModuleContainer container, String moduleName) throws InitializationException {
    try {
      @SuppressWarnings("unchecked") ICharacterModule<? extends ICharacterModuleObject> module =
        (ICharacterModule<? extends ICharacterModuleObject>) Class.forName(moduleName).newInstance();
      container.addCharacterGenericsModule(module);
    } catch (ClassNotFoundException e) {
      logger.info(moduleName + " not installed."); //$NON-NLS-1$
    } catch (InstantiationException e) {
      logger.error("Error initializing module " + moduleName, e); //$NON-NLS-1$
    } catch (IllegalAccessException e) {
      logger.error("Error initializing module " + moduleName, e); //$NON-NLS-1$
    }
  }
}
