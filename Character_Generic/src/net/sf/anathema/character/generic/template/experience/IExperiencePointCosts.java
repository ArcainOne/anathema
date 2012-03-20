package net.sf.anathema.character.generic.template.experience;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.IThaumaturgy;

public interface IExperiencePointCosts {

  public ICurrentRatingCosts getAbilityCosts(boolean favored);

  public ICurrentRatingCosts getAttributeCosts(boolean favored);

  public int getCharmCosts(ICharm charm, ICostAnalyzer costMapping);

  public int getComboCosts(ICharm[] comboCharms);

  public ICurrentRatingCosts getEssenceCosts();

  public int getSpecialtyCosts(boolean favored);

  public ICurrentRatingCosts getVirtueCosts();

  public ICurrentRatingCosts getWillpowerCosts();

  public int getSpellCosts(ISpell spell, IBasicCharacterData basicCharacter, IGenericTraitCollection traitCollection);
  
  public int getThaumaturgyCosts(IThaumaturgy thaumaturgy, IBasicCharacterData basicCharacter, IGenericTraitCollection traitCollection);

  public int getBackgroundCost();
}