package net.sf.anathema.character.generic.impl.template.points;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.charms.MartialArtsLevel;
import net.sf.anathema.character.generic.template.creation.IBonusPointCosts;
import net.sf.anathema.character.generic.template.experience.ICostAnalyzer;
import net.sf.anathema.character.generic.template.experience.ICurrentRatingCosts;
import net.sf.anathema.character.generic.traits.IFavorableGenericTrait;

public class DefaultBonusPointCosts implements IBonusPointCosts {

  @Override
  public int getSpellCosts(ICostAnalyzer costMapping) {
    boolean isSorceryFavored = costMapping.isOccultFavored();
    return getCharmCosts(isSorceryFavored, null);
  }

  @Override
  public int getCharmCosts(ICharm charm, ICostAnalyzer costMapping) {
    return getCharmCosts(costMapping.isMagicFavored(charm), costMapping.getMartialArtsLevel(charm));
  }

  protected int getCharmCosts(boolean favored, MartialArtsLevel martialArtsLevel) {
    if (martialArtsLevel == null) {
      return favored ? 4 : 5;
    }
    if (martialArtsLevel.compareTo(MartialArtsLevel.Sidereal) < 0) {
      return favored ? 4 : 5;
    }
    throw new IllegalArgumentException("Sidereal Martial Arts shan't be learned at Character Creation!"); //$NON-NLS-1$
  }

  @Override
  public ICurrentRatingCosts getAbilityCosts(boolean favored) {
    if (favored) {
      return new FixedValueRatingCosts(1);
    }
    return new FixedValueRatingCosts(2);
  }

  @Override
  public int getAttributeCosts(IFavorableGenericTrait trait) {
    return new FixedValueRatingCosts(4).getRatingCosts(trait.getCurrentValue());
  }

  @Override
  public ICurrentRatingCosts getVirtueCosts() {
    return new FixedValueRatingCosts(3);
  }

  @Override
  public int getMaximumFreeVirtueRank() {
    return 3;
  }
  
  @Override
  public int getMaximumFreeAbilityRank() {
    return 3;
  }

  @Override
  public int getWillpowerCosts() {
    return 2;
  }

  @Override
  public int getFavoredSpecialtyDotsPerPoint() {
    return 2;
  }

  @Override
  public int getDefaultSpecialtyDotsPerPoint() {
    return 1;
  }

  @Override
  public ICurrentRatingCosts getBackgroundBonusPointCost() {
    return new ThresholdRatingCosts(1, 2);
  }

  @Override
  public int getEssenceCost() {
    return 7;
  }

  @Override
  public int getMagicCosts(IMagic magic, final ICostAnalyzer analyzer) {
    final int[] cost = new int[1];
    magic.accept(new IMagicVisitor() {
      @Override
      public void visitCharm(ICharm charm) {
        cost[0] = getCharmCosts(charm, analyzer);
      }

      @Override
      public void visitSpell(ISpell spell) {
        cost[0] = getSpellCosts(analyzer);
      }

	  public void visitThaumaturgy(IThaumaturgy thaumaturgy) {
		
	  }
    });
    return cost[0];
  }
}