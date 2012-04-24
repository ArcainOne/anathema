package net.sf.anathema.character.generic.template.creation;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.template.experience.IAbilityPointCosts;
import net.sf.anathema.character.generic.template.experience.ICostAnalyzer;
import net.sf.anathema.character.generic.template.experience.ICurrentRatingCosts;
import net.sf.anathema.character.generic.traits.IFavorableGenericTrait;

public interface IBonusPointCosts extends IAbilityPointCosts, IBackgroundCreationPointCosts {

  public int getCharmCosts(ICharm charm, ICostAnalyzer analyzer);

  public int getAttributeCosts(IFavorableGenericTrait trait);

  public ICurrentRatingCosts getVirtueCosts();

  public int getWillpowerCosts();

  public int getSpellCosts(ICostAnalyzer costMapping);
  
  public int getThaumaturgyCosts(IThaumaturgy thaumaturgy, ICostAnalyzer costMapping);

  public int getEssenceCost();

  public int getMagicCosts(IMagic magic, ICostAnalyzer analyzer);

  public int getMaximumFreeVirtueRank();
}