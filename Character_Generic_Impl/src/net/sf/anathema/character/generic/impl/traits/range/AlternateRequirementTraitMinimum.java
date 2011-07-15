package net.sf.anathema.character.generic.impl.traits.range;

import net.sf.anathema.character.generic.character.ILimitationContext;
import net.sf.anathema.character.generic.impl.traits.alternate.ITraitRequirement;
import net.sf.anathema.character.generic.impl.traits.alternate.TraitRequirementCollection;
import net.sf.anathema.character.generic.traits.ITraitMinimum;
import net.sf.anathema.character.generic.traits.ITraitType;

public class AlternateRequirementTraitMinimum implements ITraitMinimum {

  private final ITraitRequirement requirement;
  private final TraitRequirementCollection requirementCollection;

  public AlternateRequirementTraitMinimum(ITraitRequirement requirement, TraitRequirementCollection collection) {
    this.requirement = requirement;
    this.requirementCollection = collection;
  }

  public int getMinimumValue(ILimitationContext limitationContext) {
    return requirementCollection.isStrictWithout(requirement, limitationContext.getTraitCollection())
        ? requirement.getFreeMinimum()
        : requirement.getStrictMinimum();
  }
  
  public int getCalculationMinValue(ILimitationContext context, ITraitType type)
  {
	  return getMinimumValue(context);
  }
  
  public int getCasteMinimumValue()
  {
	  return 0;
  }
  
  public int getFavoredMinimumValue()
  {
	  return 1;
  }
}