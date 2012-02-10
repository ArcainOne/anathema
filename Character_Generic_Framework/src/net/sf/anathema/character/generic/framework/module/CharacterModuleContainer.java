package net.sf.anathema.character.generic.framework.module;

import net.sf.anathema.character.generic.framework.CharacterGenerics;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.module.object.ICharacterModuleObject;
import net.sf.anathema.initialization.InitializationException;
import net.sf.anathema.initialization.repository.IDataFileProvider;
import net.sf.anathema.lib.resources.IResources;

public class CharacterModuleContainer {

  private CharacterGenerics characterGenerics;
  private final IResources resources;

  public CharacterModuleContainer(IResources resources, IDataFileProvider dataFileProvider)
          throws InitializationException {
    this.resources = resources;
    this.characterGenerics = new CharacterGenerics(dataFileProvider);
    addCharacterGenericsModule(new BasicExaltCharacterModule());
  }

  public void addCharacterGenericsModule(ICharacterModule<? extends ICharacterModuleObject> module)
          throws InitializationException {
    module.initModuleObject();
    module.registerCommonData(characterGenerics);
    module.addBackgroundTemplates(characterGenerics);
    module.addCharacterTemplates(characterGenerics);
    module.addAdditionalTemplateData(characterGenerics);
    module.addReportTemplates(characterGenerics, resources);
    characterGenerics.getModuleObjectMap().addModule(module);
  }

  public ICharacterGenerics getCharacterGenerics() {
    return characterGenerics;
  }
}
