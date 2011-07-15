package net.sf.anathema.character.martialarts;

import net.sf.anathema.character.generic.impl.magic.charm.special.MultipleEffectCharm;
import net.sf.anathema.character.generic.impl.magic.charm.special.TraitDependentMultiLearnableCharm;
import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.character.generic.magic.charms.special.IMultiLearnableCharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;

public interface IMartialArtsSpecialCharms {

  public static final ISpecialCharm DRAGON_CLAW_ELEMENTAL_STRIKE = new MultipleEffectCharm(
      "Terrestrial.Dragon-ClawElementalStrike", //$NON-NLS-1$
      new String[] { "Air", "Earth", "Fire", "Water", "Wood" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

  public static final ISpecialCharm TYPE_EXALT_WAYS = new MultipleEffectCharm("Sidereal.(Type)ExaltWays", new String[] { //$NON-NLS-1$
      "SolarDawn", //$NON-NLS-1$
          "SolarZenith", //$NON-NLS-1$
          "SolarTwilight", //$NON-NLS-1$
          "SolarNight", //$NON-NLS-1$
          "SolarEclipse", //$NON-NLS-1$
          "TerrestrialAir", //$NON-NLS-1$
          "TerrestrialEarth", //$NON-NLS-1$
          "TerrestrialFire", //$NON-NLS-1$
          "TerrestrialWater", //$NON-NLS-1$
          "TerrestrialWood", //$NON-NLS-1$
          "LunarFullMoon", //$NON-NLS-1$
          "LunarHalfMoon", //$NON-NLS-1$
          "LunarNoMoon", //$NON-NLS-1$
          "AbyssalDusk", //$NON-NLS-1$
          "AbyssalMidnight", //$NON-NLS-1$
          "AbyssalDaybreak", //$NON-NLS-1$
          "AbyssalDay", //$NON-NLS-1$
          "AbyssalMoonshadow", //$NON-NLS-1$
          "SiderealJourneys", //$NON-NLS-1$
          "SiderealSerenity", //$NON-NLS-1$
          "SiderealBattles", //$NON-NLS-1$
          "SiderealSecrets", //$NON-NLS-1$
          "SiderealEndings", //$NON-NLS-1$
          "AlchemicalOrichalcum", //$NON-NLS-1$
          "AlchemicalMoonsilver", //$NON-NLS-1$
          "AlchemicalJade", //$NON-NLS-1$
          "AlchemicalStarmetal", //$NON-NLS-1$
          "AlchemicalSoulsteel", //$NON-NLS-1$
          "AlchemicalAdamant" }); //$NON-NLS-1$

  public static final IMultiLearnableCharm WRITHING_BLOOD_CHAIN_TECHNIQUE = new TraitDependentMultiLearnableCharm(
	      "Abyssal.WrithingBloodChainTechnique", //$NON-NLS-1$
	      EssenceTemplate.SYSTEM_ESSENCE_MAX,
	      OtherTraitType.Essence, -2);
}
