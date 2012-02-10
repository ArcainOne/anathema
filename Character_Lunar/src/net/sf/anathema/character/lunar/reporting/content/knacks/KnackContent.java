package net.sf.anathema.character.lunar.reporting.content.knacks;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.lunar.reporting.content.stats.knacks.IKnackStats;
import net.sf.anathema.character.lunar.reporting.content.stats.knacks.KnackNameStatsGroup;
import net.sf.anathema.character.lunar.reporting.content.stats.knacks.KnackSourceStatsGroup;
import net.sf.anathema.character.lunar.reporting.content.stats.knacks.KnackStats;
import net.sf.anathema.character.reporting.pdf.content.AbstractSubBoxContent;
import net.sf.anathema.character.reporting.pdf.content.stats.IStatsGroup;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;

import java.util.ArrayList;
import java.util.List;

public class KnackContent extends AbstractSubBoxContent {

  private static IIdentificate KNACK = new Identificate("Knack");

  private IGenericCharacter character;

  public KnackContent(IResources resources, IGenericCharacter character) {
    super(resources);
    this.character = character;
  }

  public IStatsGroup<IKnackStats>[] createStatsGroups() {
    IResources resources = getResources();
    return new IStatsGroup[]{new KnackNameStatsGroup(resources), new KnackSourceStatsGroup(resources)};
  }

  public List<IKnackStats> createPrintKnacks() {
    final List<IKnackStats> printKnacks = new ArrayList<IKnackStats>();
    ICharm[] charmSet = character.getLearnedCharms();
    for (ICharm charm : charmSet) {
      if (!charm.hasAttribute(KNACK)) {
        continue;
      }
      printKnacks.add(new KnackStats(charm));
    }
    return printKnacks;
  }

  @Override
  public String getHeaderKey() {
    return "Lunar.Knacks";
  }

  public String getGroupLabel(IKnackStats stats) {
    return  stats.getGroupName(getResources());
  }
}
