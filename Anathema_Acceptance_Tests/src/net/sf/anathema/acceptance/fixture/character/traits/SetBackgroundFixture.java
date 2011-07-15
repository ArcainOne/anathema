package net.sf.anathema.acceptance.fixture.character.traits;

import net.sf.anathema.acceptance.fixture.character.CharacterSummary;
import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.model.ICharacter;
import net.sf.anathema.character.model.ICharacterStatistics;

public class SetBackgroundFixture extends AbstractBackgroundFixture {

  public int value;

  @Override
  public void enterRow() throws Exception {
    IDefaultTrait trait = (IDefaultTrait) getTrait();
    if (value == 0 && trait == null) {
      return;
    }
    if (trait == null) {
      trait = getCharacterStatistics().getTraitConfiguration().getBackgrounds().addBackground(getTraitType(), null);
    }
    trait.setCurrentValue(value);
  }

  @SuppressWarnings("unchecked")
protected final ITrait getTrait() {
    ICharacter character = new CharacterSummary(summary).getCharacter();
    ICharacterStatistics statistics = character.getStatistics();
    return statistics.getTraitConfiguration().getTrait(getTraitType());
  }
}