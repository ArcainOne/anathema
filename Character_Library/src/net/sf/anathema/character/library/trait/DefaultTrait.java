package net.sf.anathema.character.library.trait;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterListening;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ITraitContext;
import net.sf.anathema.character.library.ITraitFavorization;
import net.sf.anathema.character.library.trait.favorable.IIncrementChecker;
import net.sf.anathema.character.library.trait.favorable.NullTraitFavorization;
import net.sf.anathema.character.library.trait.favorable.TraitFavorization;
import net.sf.anathema.character.library.trait.rules.IFavorableTraitRules;
import net.sf.anathema.character.library.trait.rules.ITraitRules;
import net.sf.anathema.character.library.trait.visitor.ITraitVisitor;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.data.Range;

public class DefaultTrait extends AbstractFavorableTrait implements IFavorableDefaultTrait {

  private final ChangeControl rangeControl = new ChangeControl();
  private int capModifier = 0;
  private int creationValue;
  private int experiencedValue = ITraitRules.UNEXPERIENCED;
  private final IValueChangeChecker checker;
  private ITraitFavorization traitFavorization;
  private final ICharacterChangeListener changeListener = new DedicatedCharacterChangeAdapter() {
    @Override
    public void casteChanged() {
      resetCurrentValue();
      getFavorization().updateFavorableStateToCaste();
    }
  };

  public DefaultTrait(
      IFavorableTraitRules traitRules,
      ICasteType[] castes,
      ITraitContext traitContext,
      IBasicCharacterData basicData,
      ICharacterListening listening,
      IValueChangeChecker valueChangeChecker,
      IIncrementChecker favoredIncrementChecker) {
    this(traitRules, traitContext, valueChangeChecker);
    setTraitFavorization(new TraitFavorization(
        basicData,
        castes,
        favoredIncrementChecker,
        this,
        traitRules.getCasteMinimumValue(),
        traitRules.getFavoredMinimumValue(),
        traitRules.isRequiredFavored()));
    listening.addChangeListener(changeListener);
    getFavorization().updateFavorableStateToCaste();
  }

  public DefaultTrait(ITraitRules traitRules, ITraitContext traitContext, IValueChangeChecker checker) {
    super(traitRules, traitContext);
    setTraitFavorization(new NullTraitFavorization());
    this.checker = checker;
    this.creationValue = traitRules.getStartValue();
  }
  
  public void applyCapModifier(int modifier)
  {
	  capModifier += modifier;
	  getTraitRules().setCapModifier(capModifier);
  }
  
  public int getUnmodifiedMaximalValue()
  {
	  return getTraitRules().getCurrentMaximumValue(false);
  }
  
  public int getModifiedMaximalValue()
  {
	  return getTraitRules().getCurrentMaximumValue(true);
  }

  protected void setTraitFavorization(ITraitFavorization favorization) {
    this.traitFavorization = favorization;
  }

  public final int getCreationValue() {
    return creationValue;
  }

  public ITraitFavorization getFavorization() {
    return traitFavorization;
  }

  public void setCreationValue(int value) {
    if (getFavorization().isCasteOrFavored()) {
      value = Math.max(value, getFavorization().getMinimalValue());
    }
    int correctedValue = getTraitRules().getCreationValue(value);
    if (this.creationValue == correctedValue) {
      return;
    }
    this.creationValue = correctedValue;
    getCreationPointControl().fireValueChangedEvent(this.creationValue);
    getTraitValueStrategy().notifyOnCreationValueChange(getCurrentValue(), getCurrentValueControl());
  }
  
  public void setUncheckedCreationValue(int value)
  {
	  if (this.creationValue == value)
	      return;
	  this.creationValue = value;
	  getCreationPointControl().fireValueChangedEvent(this.creationValue);
	  getTraitValueStrategy().notifyOnCreationValueChange(getCurrentValue(), getCurrentValueControl());
  }

  public final void resetCreationValue() {
    setCreationValue(getCreationValue());
  }

  public final void resetExperiencedValue() {
	if (getExperiencedValue() != ITraitRules.UNEXPERIENCED)
		setExperiencedValue(Math.max(getCreationValue(), getExperiencedValue()));
  }

  public final void addRangeListener(IChangeListener listener) {
    rangeControl.addChangeListener(listener);
  }

  @Override
  public String toString() {
    return getType() + ":" + getCreationValue(); //$NON-NLS-1$
  }

  public int getCurrentValue() {
    return getTraitValueStrategy().getCurrentValue(this);
  }

  public final int getCalculationValue() {
    return getTraitValueStrategy().getCalculationValue(this);
  }

  public int getCreationCalculationValue() {
    return Math.max(getCurrentValue(), getZeroCalculationValue());
  }

  public int getExperiencedCalculationValue() {
    return getTraitRules().getExperienceCalculationValue(creationValue, experiencedValue, getCurrentValue());
  }

  public void setCurrentValue(int value) {
    if (!checker.isValidNewValue(value)) {
      resetCurrentValue();
    }
    else {
      if (value == getCurrentValue()) {
        return;
      }
      getTraitValueStrategy().setValue(this, value);
    }
  }

  public final int getExperiencedValue() {
    return experiencedValue;
  }

  public final void setExperiencedValue(int value) {
    int correctedValue = getTraitRules().getExperiencedValue(getCreationValue(), value);
    if (correctedValue == experiencedValue) {
      return;
    }
    this.experiencedValue = correctedValue;
    getTraitValueStrategy().notifyOnLearnedValueChange(getCurrentValue(), getCurrentValueControl());
  }
  
  public final void setUncheckedExperiencedValue(int value)
  {
	if (value == experiencedValue)
	   return;
	this.experiencedValue = value;
	getTraitValueStrategy().notifyOnLearnedValueChange(getCurrentValue(), getCurrentValueControl());
   }

  public final void resetCurrentValue() {
    getTraitValueStrategy().resetCurrentValue(this);
  }

  public void setModifiedCreationRange(int lowerBound, int upperBound) {
    getTraitRules().setModifiedCreationRange(new Range(lowerBound, upperBound));
    resetCreationValue();
  }

  public void accept(ITraitVisitor visitor) {
    visitor.visitDefaultTrait(this);
  }

  public final int getMinimalValue() {
    return getTraitValueStrategy().getMinimalValue(this);
  }
  
  public int getMaxFree()
  {
	  return getTraitRules().getMaxFree();
  }
  
  public final int getCalculationMinValue()
  {
	return getTraitRules().getCalculationMinValue();
  }
}