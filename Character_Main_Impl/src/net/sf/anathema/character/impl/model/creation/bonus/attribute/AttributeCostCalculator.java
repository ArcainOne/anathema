package net.sf.anathema.character.impl.model.creation.bonus.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.anathema.character.generic.template.creation.IBonusPointCosts;
import net.sf.anathema.character.generic.template.points.AttributeGroupPriority;
import net.sf.anathema.character.generic.template.points.IAttributeCreationPoints;
import net.sf.anathema.character.generic.template.points.IAttributeGroupPriorityVisitor;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.groups.IIdentifiedTraitTypeGroup;
import net.sf.anathema.character.generic.traits.groups.IdentifiedTraitTypeGroup;
import net.sf.anathema.character.generic.traits.types.AttributeGroupType;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.character.impl.model.creation.bonus.additional.IAdditionalBonusPointManagment;
import net.sf.anathema.character.impl.model.creation.bonus.basic.ElementCreationCost;
import net.sf.anathema.character.impl.model.creation.bonus.basic.ElementCreationCostCalculator;
import net.sf.anathema.character.impl.model.creation.bonus.basic.ICostElement;
import net.sf.anathema.character.impl.model.creation.bonus.trait.TraitCostElement;
import net.sf.anathema.character.impl.model.creation.bonus.util.TraitGroupCost;
import net.sf.anathema.character.impl.model.creation.bonus.util.TraitSorter;
import net.sf.anathema.character.library.trait.AbstractFavorableTraitCostCalculator;
import net.sf.anathema.character.library.trait.IFavorableDefaultTrait;
import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.character.library.trait.TraitGroup;
import net.sf.anathema.character.library.trait.favorable.IFavorableTrait;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.model.traits.ICoreTraitConfiguration;
import net.sf.anathema.lib.util.Identificate;

public class AttributeCostCalculator extends AbstractFavorableTraitCostCalculator implements IAttributeCostCalculator
{
  private static IFavorableTrait[] getAllAttributes(ICoreTraitConfiguration traitConfiguration)
  {
	  List<ITraitType> attributeTypes = new ArrayList<ITraitType>();
	  for (IIdentifiedTraitTypeGroup group : traitConfiguration.getAttributeTypeGroups()) {
	    Collections.addAll(attributeTypes, group.getAllGroupTypes());
	  }
	  return traitConfiguration.getFavorableTraits(attributeTypes.toArray(new ITraitType[attributeTypes.size()]));
  }
  
  private final Map<ITrait, ElementCreationCost> costsByAttribute = new HashMap<ITrait, ElementCreationCost>();
  private List<TraitGroupCost> orderedGroups;
  private TraitGroup[] traitGroups;
  private final ICoreTraitConfiguration traitConfiguration;
  private final IBonusPointCosts costs;
  private final List<List<TraitGroup>> priorityPermutations = new ArrayList<List<TraitGroup>>();
  private final int sortingBonusCostScaleFactor = 1000;
  private final boolean isCasteAxis;

  public AttributeCostCalculator(
      ICoreTraitConfiguration traitConfiguration,
      IAttributeCreationPoints points,
      IBonusPointCosts costs,
      IAdditionalBonusPointManagment additionalPools) {
	  super(additionalPools, points, costs.getMaximumFreeAbilityRank(), getAllAttributes(traitConfiguration));
	  this.isCasteAxis = points.isCasteAxis();
	  this.traitConfiguration = traitConfiguration;
	  this.traitGroups = createTraitGroups();
	  this.costs = costs;
	  createPermutations(new ArrayList<TraitGroup>());
  }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private TraitGroup[] createTraitGroups() {
	  if (!isCasteAxis)
	  {
	    IIdentifiedTraitTypeGroup[] attributeTypeGroups = traitConfiguration.getAttributeTypeGroups();
	    TraitGroup[] newGroups = new TraitGroup[attributeTypeGroups.length];
	    for (int index = 0; index < newGroups.length; index++) {
	      newGroups[index] = new TraitGroup(traitConfiguration, attributeTypeGroups[index]);
	    }
	    return newGroups;
	  }
	  else
	  {
		  List[] attributeLists = new ArrayList[3];
		  TraitGroup[] newGroups = new TraitGroup[3];
		  for (int i = 0; i != 3; i++)
			  attributeLists[i] = new ArrayList<AttributeType>();
		  for (AttributeType attribute : AttributeType.values())
		  {
			  IFavorableTrait trait = traitConfiguration.getFavorableTrait(attribute);
			  int index = 2;
			  index = trait.getFavorization().isCaste() ? 0 : index;
			  index = trait.getFavorization().isFavored() ? 1 : index;
			  attributeLists[index].add(attribute);
		  }
		  for (int i = 0; i != attributeLists.length; i++)
		  {
			  Object[] array = new AttributeType[attributeLists[i].size()];
			  attributeLists[i].toArray(array);
			  IIdentifiedTraitTypeGroup group = new IdentifiedTraitTypeGroup((ITraitType[]) array, new Identificate(""));
			  newGroups[i] = new TraitGroup(traitConfiguration, group);
		  }
		  return newGroups;  
	  }
  }
  
  private void createPermutations(List<TraitGroup> parent)
  {
	  boolean isLeaf = true;
	  for (TraitGroup entry : traitGroups)
	  {
		  if (parent.contains(entry))
			  continue;
		  isLeaf = false;
		  List<TraitGroup> child = new ArrayList<TraitGroup>(parent);
		  child.add(entry);
		  createPermutations(child);
	  }
	  if (isLeaf)
		  priorityPermutations.add(parent);
  }

  public void calculateAttributeCosts()
  {
	  clear();
	  costsByAttribute.clear();
	  countFavoredTraits();
	  
	  IAttributeCreationPoints attributeCreation = (IAttributeCreationPoints) points;
	  if (!isCasteAxis)
	  {
		  int bestCost = Integer.MAX_VALUE;
		  int bestPermutation = 0;
		  
		  for (int i = 0; i != priorityPermutations.size(); i++)
		  {
			  List<TraitGroup> permutation = priorityPermutations.get(i);
			  int cost = calculateCost(attributeCreation, permutation, false);
			  if (cost < bestCost)
			  {
				  bestCost = cost;
				  bestPermutation = i;
			  }
		  }
		  List<TraitGroup> bestOrder = priorityPermutations.get(bestPermutation);
		  orderedGroups = createGroupCost(attributeCreation, bestOrder);
		  calculateCost(attributeCreation, bestOrder, true);
	  }
	  else
	  {
		  traitGroups = createTraitGroups();
		  List<TraitGroup> groupOrder = new ArrayList<TraitGroup>();
		  for (TraitGroup group : traitGroups)
			  groupOrder.add(group);
		  orderedGroups = createGroupCost(attributeCreation, groupOrder);
		  calculateCost(attributeCreation, groupOrder, true);
	  }
	  
  }
  
  private int calculateCost(IAttributeCreationPoints points,
		  List<TraitGroup> permutation,
		  boolean record)
  {
	  int bonusCost = 0;
	  int wastedFreeDots = 0;
	  int extraFavoredDotsLeft = this.getExtraFavoredDotCount();
	  int extraGenericDotsLeft = this.getExtraGenericDotCount();
	  
	  for (int i = 0; i != permutation.size(); i++)
	  {
		  TraitGroup group = permutation.get(i);
		  int freePointsLeft = points.getCounts()[i];
		  boolean favoredInGroup = false;
		  
		  IFavorableTrait[] unsortedTraits = group.getGroupTraits();
		  int[] sums = new int[unsortedTraits.length];
		  for (int j = 0; j != unsortedTraits.length; j++)
			  sums[j] = costs.getAttributeCosts(unsortedTraits[j]);
		  
		  List<IFavorableTrait> orderedTraits = getSortedTraits(unsortedTraits, sums);
	      for (IFavorableTrait attribute : orderedTraits)
	      {
	    	  favoredInGroup = favoredInGroup || (attribute.isCasteOrFavored() &&
	    			  attribute.getCurrentValue() > attribute.getInitialValue());
		      int costFactor = costs.getAttributeCosts(attribute);
		      ElementCreationCost cost = handleAttribute((IDefaultTrait) attribute, freePointsLeft,
		    		  favoredInGroup ? extraFavoredDotsLeft : 0, extraGenericDotsLeft, costFactor);
		      freePointsLeft -= cost.getDotsSpent();
		      extraFavoredDotsLeft -= cost.getExtraFavoredDotsSpent();
		      extraGenericDotsLeft -= cost.getExtraGenericDotsSpent();
		      bonusCost += cost.getBonusPointsSpent();
		      if (record)
		      {
		    	  increaseExtraFavoredDotSum(cost.getExtraFavoredDotsSpent());
		    	  increaseExtraGenericDotSum(cost.getExtraGenericDotsSpent());
		    	  costsByAttribute.put(attribute, cost);
		    	  orderedGroups.get(i).addTraitToCost(attribute, cost);
		      }
	      }
	      wastedFreeDots += freePointsLeft * freePointsLeft;
	  }
	  return sortingBonusCostScaleFactor * bonusCost + wastedFreeDots;
  }

  private ElementCreationCost handleAttribute(IDefaultTrait attribute, int freeDots, int extraFavoredDots, int extraGenericDots,
		  	int bonusPointCostFactor) {
    ICostElement element = new TraitCostElement(attribute);
    return new ElementCreationCostCalculator().calculateElementCreationCost(element, freeDots, extraFavoredDots, extraGenericDots, bonusPointCostFactor);
  }
  
  private List<TraitGroupCost> createGroupCost(IAttributeCreationPoints points,
		  List<TraitGroup> groups)
  {
	  List<TraitGroupCost> priorizedGroups = new ArrayList<TraitGroupCost>(traitGroups.length);
	  for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
	    priorizedGroups.add(new TraitGroupCost(groups.get(groupIndex), points.getCounts()[groupIndex]));
	  }
	  return priorizedGroups; 
  }

  private List<IFavorableTrait> getSortedTraits(IFavorableTrait[] traits, int[] groupSums) {
    return new TraitSorter().sortDescending(traits, groupSums);
  }

  public TraitGroupCost getAttributePoints(AttributeGroupPriority priority) {
    final TraitGroupCost[] cost = new TraitGroupCost[1];
    priority.accept(new IAttributeGroupPriorityVisitor() {
      public void acceptTertiary() {
        cost[0] = orderedGroups.get(2);
      }

      public void acceptSecondary() {
        cost[0] = orderedGroups.get(1);
      }

      public void acceptPrimary() {
        cost[0] = orderedGroups.get(0);
      }
    });
    return cost[0];
  }

  public TraitGroupCost getAttributePoints(AttributeGroupType attributeGroupType) {
    for (TraitGroupCost cost : orderedGroups) {
      if (cost.getGroup().getGroupId() == attributeGroupType) {
        return cost;
      }
    }
    throw new IllegalArgumentException("Unknown Attribute Group"); //$NON-NLS-1$
  }

  public int getBonusPoints() {
    int pointsSpent = 0;
    for (TraitGroupCost cost : orderedGroups) {
      pointsSpent += cost.getBonusPointsSpent();
    }
    return pointsSpent;
  }

	public ElementCreationCost getCosts(IDefaultTrait attribute)
	{
	  return costsByAttribute.get(attribute);
	}

	@Override
	protected int getCostFactor(IFavorableDefaultTrait trait)
	{
	    int costFactor = costs.getAttributeCosts(trait);
	    return costFactor;
	}
}
