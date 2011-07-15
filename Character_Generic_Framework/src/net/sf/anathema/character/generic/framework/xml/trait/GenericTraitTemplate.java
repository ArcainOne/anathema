package net.sf.anathema.character.generic.framework.xml.trait;

import net.sf.anathema.character.generic.character.ILimitationContext;
import net.sf.anathema.character.generic.template.ITraitLimitation;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.LowerableState;
import net.sf.anathema.lib.lang.clone.ReflectionCloneableObject;

public class GenericTraitTemplate extends ReflectionCloneableObject<IClonableTraitTemplate> implements
    IClonableTraitTemplate {

  private Integer minimumValue;
  private Integer casteMinimumValue = 0;
  private Integer favoredMinimumValue = 0;
  private Integer zeroLevelValue;
  private Integer startValue = 0;
  private LowerableState lowerableState;
  private ITraitLimitation limitation;
  private boolean isRequiredFavored;
  private boolean isFreebie;
  private String tag;
  
  public String getTag()
  {
	return tag;
  }

  public ITraitLimitation getLimitation() {
    return limitation;
  }

  public LowerableState getLowerableState() {
    return lowerableState;
  }

  public int getStartValue() {
    return startValue;
  }

  public int getZeroLevelValue() {
    return zeroLevelValue;
  }

  public int getMinimumValue(ILimitationContext limitationContext) {
    return minimumValue;
  }
  
  public int getCasteMinimumValue()
  {
	  return casteMinimumValue;
  }
  
  public int getFavoredMinimumValue()
  {
	  return favoredMinimumValue;
  }

  public final void setLimitation(ITraitLimitation limitation) {
    this.limitation = limitation;
  }

  public final void setLowerableState(LowerableState lowerableState) {
    this.lowerableState = lowerableState;
  }

  public final void setMinimumValue(int minimumValue) {
    this.minimumValue = minimumValue;
  }
  
  public final void setCasteMinimumValue(int minimumValue) {
	    this.casteMinimumValue = minimumValue;
	  }
  
  public final void setFavoredMinimumValue(int minimumValue) {
	    this.favoredMinimumValue = minimumValue;
	  }

  public final void setStartValue(int startValue) {
	  if (startValue > this.startValue)
		  this.startValue = startValue;
  }

  public final void setZeroLevelValue(int zeroLevelValue) {
    this.zeroLevelValue = zeroLevelValue;
  }

  @Override
  public GenericTraitTemplate clone() {
    return (GenericTraitTemplate) super.clone();
  }

  public void setRequiredFavored(boolean isRequiredFavored) {
    this.isRequiredFavored = isRequiredFavored;
  }

  public boolean isRequiredFavored() {
    return isRequiredFavored;
  }
  
  public void setIsFreebie(boolean value)
  {
	  isFreebie = value;
  }
  
  public void setTag(String tag)
  {
	  this.tag = tag;
  }
  
  public int getCalculationMinValue(ILimitationContext context, ITraitType type)
  {
	  return isFreebie ? getMinimumValue(context) : 0;
  }
}