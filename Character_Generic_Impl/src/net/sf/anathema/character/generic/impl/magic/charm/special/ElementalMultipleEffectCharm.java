package net.sf.anathema.character.generic.impl.magic.charm.special;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.magic.charms.special.IMultipleEffectCharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmVisitor;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;

public class ElementalMultipleEffectCharm implements IMultipleEffectCharm {

  private final String charmId;
  private final List<ElementalSubeffect> effectList = new ArrayList<ElementalSubeffect>();

  public ElementalMultipleEffectCharm(String charmId) {
    this.charmId = charmId;
  }

  public void accept(ISpecialCharmVisitor visitor) {
    visitor.visitMultipleEffectCharm(this);
  }

  public String getCharmId() {
    return charmId;
  }

  public ISubeffect[] buildSubeffects(IBasicCharacterData data,
		  IGenericTraitCollection traitCollection,
		  ICharmLearnableArbitrator arbitrator,
		  ICharm charm) {
    if (effectList.isEmpty()) {
      for (Elements element : Elements.values()) {
        effectList.add(new ElementalSubeffect(element, data, buildLearnCondition(element, data, arbitrator, charm)));
      }
    }
    return effectList.toArray(new ISubeffect[effectList.size()]);
  }

  private ICondition buildLearnCondition(
      final Elements element,
      final IBasicCharacterData data,
      final ICharmLearnableArbitrator arbitrator,
      final ICharm charm) {
    return new ICondition() {
      public boolean isFullfilled() {
        boolean learnable = arbitrator.isLearnable(charm);
        if (!data.getCharacterType().equals(CharacterType.DB)) {
          return learnable;
        }
        if (element.equals(data.getCasteType())) {
          return learnable;
        }
        for (ElementalSubeffect effect : effectList) {
          if (effect.isLearned() && effect.matches(data.getCasteType())) {
            return learnable;
          }
        }
        return false;
      }
    };
  }
  
  private class ElementalSubeffect extends Subeffect
  {
	  private final Elements aspect;

	  public ElementalSubeffect(Elements aspect, IBasicCharacterData data, ICondition learnable) {
	    super(aspect.getId(), data, learnable);
	    this.aspect = aspect;
	  }

	  public boolean matches(ICasteType casteType) {
	    return aspect.equals(casteType);
	  }
	}
  
  private enum Elements
  {
  	  Air, Earth, Fire, Water, Wood;

  	  public String getId() {
  	    return name();
  	  }

  	  @Override
  	  public String toString() {
  	    return name();
  	  }
  }
}