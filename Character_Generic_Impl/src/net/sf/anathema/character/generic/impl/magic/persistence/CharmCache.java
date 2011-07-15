package net.sf.anathema.character.generic.impl.magic.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.anathema.character.generic.impl.magic.Charm;
import net.sf.anathema.character.generic.impl.rules.ExaltedRuleSet;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.lib.collection.MultiEntryMap;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;

public class CharmCache implements ICharmCache {

  private static final CharmCache instance = new CharmCache();
  private final Map<IExaltedRuleSet, MultiEntryMap<IIdentificate, ICharm>> charmSetsByRuleSet = new HashMap<IExaltedRuleSet, MultiEntryMap<IIdentificate, ICharm>>();
  private final Map<IExaltedRuleSet, Map<IIdentificate, List<ISpecialCharm>>> specialCharms = new HashMap<IExaltedRuleSet, Map<IIdentificate, List<ISpecialCharm>>>();
  private final Map<IExaltedRuleSet, Map<String, String>> renameData = new HashMap<IExaltedRuleSet, Map<String, String>>();

  private CharmCache() {
    for (IExaltedRuleSet ruleset : ExaltedRuleSet.values()) {
      charmSetsByRuleSet.put(ruleset, new MultiEntryMap<IIdentificate, ICharm>());
      renameData.put(ruleset, new HashMap<String, String>());
      
      Map<IIdentificate, List<ISpecialCharm>> list = new HashMap<IIdentificate, List<ISpecialCharm>>();
      specialCharms.put(ruleset, list);
      for (CharacterType type : CharacterType.values())
    	  list.put(type, new ArrayList<ISpecialCharm>());  
      list.put(new Identificate("MartialArts"), new ArrayList<ISpecialCharm>());
    }
  }

  public static CharmCache getInstance() {
    return instance;
  }

  public ICharm[] getCharms(IIdentificate type, IExaltedRuleSet ruleset) {
    List<ICharm> charmList = charmSetsByRuleSet.get(ruleset).get(type);
    return charmList.toArray(new ICharm[charmList.size()]);
  }

  public void addCharm(IIdentificate type, IExaltedRuleSet rules, ICharm charm) {
    MultiEntryMap<IIdentificate, ICharm> ruleMap = charmSetsByRuleSet.get(rules);
    ruleMap.replace(type, charm, charm);
  }
  public void addCharm(ICharmEntryData charmData) {
    ICharm charm = new Charm(charmData.getCoreData());
    addCharm(charm.getCharacterType(), charmData.getEdition().getDefaultRuleset(), charm);
  }
  public boolean isEmpty() {
    for (Entry<IExaltedRuleSet, MultiEntryMap<IIdentificate, ICharm>> entry : charmSetsByRuleSet.entrySet()) {
      if (!entry.getValue().keySet().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public void cloneCharms(IExaltedRuleSet sourceRules, ExaltedRuleSet targetRules) {
    MultiEntryMap<IIdentificate, ICharm> sourceMap = charmSetsByRuleSet.get(sourceRules);
    MultiEntryMap<IIdentificate, ICharm> targetMap = charmSetsByRuleSet.get(targetRules);
    for (IIdentificate type : sourceMap.keySet()) {
      for (ICharm charm : sourceMap.get(type)) {
        targetMap.add(type, ((Charm) charm).cloneUnconnected());
      }
    }
  }

  public Iterable<ICharm> getCharms(IExaltedRuleSet rules) {
    MultiEntryMap<IIdentificate, ICharm> ruleMap = charmSetsByRuleSet.get(rules);
    List<ICharm> allCharms = new ArrayList<ICharm>();
    for (IIdentificate type : ruleMap.keySet()) {
      for (ICharm charm : ruleMap.get(type)) {
        allCharms.add(charm);
      }
    }
    return allCharms;
  }
  
  public ISpecialCharm[] getSpecialCharmData(IIdentificate type, IExaltedRuleSet ruleset) {
	    List<ISpecialCharm> charmList = specialCharms.get(ruleset).get(type);
	    return charmList.toArray(new ISpecialCharm[charmList.size()]);
	  }
  
  public void addSpecialCharmData(IExaltedRuleSet ruleSet, IIdentificate type, List<ISpecialCharm> data)
  {
	  if (data == null)
		  return;
	  Map<IIdentificate, List<ISpecialCharm>> rulesetMap = specialCharms.get(ruleSet);
	  List<ISpecialCharm> list = rulesetMap.get(type);
	  list.addAll(data);
  }
  
  public void addCharmRenames(IExaltedRuleSet ruleSet, Map<String, String> mappings)
  {
	  if (mappings == null) return;
	  Map<String, String> rulesetMap = renameData.get(ruleSet);
	  rulesetMap.putAll(mappings);
  }
  
  public String getCharmRename(IExaltedRuleSet rules, String name)
  {
	  String newName = name;
	  do
	  {
		  name = newName;
		  newName = renameData.get(rules).get(name);
	  }
	  while (newName != null);
	  return name;
	  
  }
}