package net.sf.anathema.character.mutations.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.impl.rules.ExaltedSourceBook;
import net.sf.anathema.character.generic.rules.IExaltedEdition;

public class MutationProvider {
	
  public static IMutation[] getMutations(IExaltedEdition edition, IMutationRules rules)
  {
	  List<IMutation> mutations = null;
	  List<IMutation> toRemove = new ArrayList<IMutation>();
	  if (edition == ExaltedEdition.FirstEdition)
		  mutations = getFirstEditionMutations();
	  if (edition == ExaltedEdition.SecondEdition)
		  mutations = getSecondEditionMutations();
	  
	  for (IMutation mutation : mutations)
		  if (!rules.acceptMutation(mutation))
			  toRemove.add(mutation);
	  
	  mutations.removeAll(toRemove);
	  
	  return mutations.toArray(new IMutation[mutations.size()]);
  }
  
  private static List<IMutation> getSecondEditionMutations()
  {
	  List<IMutation> Mutations = new ArrayList<IMutation>();
	  Mutations.add(new Mutation("EnhancedSense", MutationType.Pox, ExaltedSourceBook.SecondEdition, 288));
	  Mutations.add(new Mutation("Claws", MutationType.Pox, ExaltedSourceBook.SecondEdition, 288));
	  Mutations.add(new Mutation("Fangs", MutationType.Pox));
	  Mutations.add(new Mutation("FurFeathersLeavesScales", MutationType.Pox));
	  Mutations.add(new Mutation("Hooves", MutationType.Pox));
	  Mutations.add(new Mutation("SerpentineTongue", MutationType.Pox));
	  Mutations.add(new Mutation("SkinHair", MutationType.Pox));
	  Mutations.add(new Mutation("Small", MutationType.Pox));
	  Mutations.add(new Mutation("Tail", MutationType.Pox));
	  Mutations.add(new Mutation("ThirdEye", MutationType.Pox));
	  Mutations.add(new Mutation("WolfsPace", MutationType.Pox));
	  Mutations.add(new Mutation("ChakraEye", MutationType.Affliction));
	  Mutations.add(new Mutation("Chameleon", MutationType.Affliction));
	  Mutations.add(new Mutation("FrogTongue", MutationType.Affliction));
	  Mutations.add(new Mutation("GazellesPace", MutationType.Affliction));
	  Mutations.add(new Mutation("Gills", MutationType.Affliction));
	  Mutations.add(new Mutation("PrehensileTail", MutationType.Affliction));
	  Mutations.add(new Mutation("ScorpionsTail", MutationType.Affliction));
	  Mutations.add(new Mutation("TalonsTusksHorns", MutationType.Affliction));
	  Mutations.add(new Mutation("ThickSkin", MutationType.Affliction));
	  Mutations.add(new Mutation("Toxin", MutationType.Affliction));
	  Mutations.add(new Mutation("AcidicPustules", MutationType.Blight));
	  Mutations.add(new Mutation("ArmoredHide", MutationType.Blight));
	  Mutations.add(new Mutation("CheetahsPace", MutationType.Blight));
	  Mutations.add(new Mutation("Glider", MutationType.Blight));
	  Mutations.add(new Mutation("HideousMaw", MutationType.Blight));
	  Mutations.add(new Mutation("LidlessDemonEye", MutationType.Blight));
	  Mutations.add(new Mutation("PrehensileBodyHair", MutationType.Blight));
	  Mutations.add(new Mutation("Quills", MutationType.Blight));
	  Mutations.add(new Mutation("SerpentineHair", MutationType.Blight));
	  Mutations.add(new Mutation("Tentacles", MutationType.Blight));
	  Mutations.add(new Mutation("WallWalking", MutationType.Blight));
	  Mutations.add(new Mutation("Hive", MutationType.Abomination));
	  Mutations.add(new Mutation("ExtraArmLegHead", MutationType.Abomination));
	  Mutations.add(new Mutation("SerpentsBody", MutationType.Abomination));
	  Mutations.add(new Mutation("SpiderLegs", MutationType.Abomination));
	  Mutations.add(new Mutation("TerrifyingMane", MutationType.Abomination));
	  Mutations.add(new Mutation("Wings", MutationType.Abomination));
	  
	  Mutations.add(new Mutation("Atrophy", MutationType.Deficiency));
	  
	  return Mutations;
  }
  
  private static List<IMutation> getFirstEditionMutations()
  {
	  return null;
  }
}