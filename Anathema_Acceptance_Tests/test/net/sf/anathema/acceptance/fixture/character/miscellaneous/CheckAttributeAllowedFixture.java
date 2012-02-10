package net.sf.anathema.acceptance.fixture.character.miscellaneous;

import net.sf.anathema.acceptance.fixture.character.util.AbstractCharacterColumnFixture;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.generic.traits.types.ValuedTraitType;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;

public class CheckAttributeAllowedFixture extends AbstractCharacterColumnFixture {

  public int appearanceValue;
  public int essenceValue;

  public boolean isAppearanceValueAllowed() {
    getAppearance().setCurrentValue(0);
    getEssence().setCurrentValue(essenceValue);
    ValuedTraitType appearance = new ValuedTraitType(AttributeType.Appearance, appearanceValue);
    IGenericTraitCollection traitCollection = getCharacterStatistics().getCharacterContext()
        .getTraitContext()
        .getLimitationContext()
        .getTraitCollection();
    return getCharacterStatistics().getCharacterTemplate()
        .getAdditionalRules()
        .getAdditionalTraitRules()
        .isAllowedTraitValue(appearance, traitCollection);

  }

  private IDefaultTrait getEssence() {
    return (IDefaultTrait) getCharacterStatistics().getTraitConfiguration().getTrait(OtherTraitType.Essence);
  }

  public boolean isEssenceValueAllowed() {
    getAppearance().setCurrentValue(appearanceValue);
    ValuedTraitType essence = new ValuedTraitType(OtherTraitType.Essence, essenceValue);
    IGenericTraitCollection collection = getCharacterStatistics().getCharacterContext()
        .getTraitContext()
        .getLimitationContext()
        .getTraitCollection();
    return getCharacterStatistics().getCharacterTemplate()
        .getAdditionalRules()
        .getAdditionalTraitRules()
        .isAllowedTraitValue(essence, collection);
  }

  private IDefaultTrait getAppearance() {
    return (IDefaultTrait) getCharacterStatistics().getTraitConfiguration().getTrait(AttributeType.Appearance);
  }
}