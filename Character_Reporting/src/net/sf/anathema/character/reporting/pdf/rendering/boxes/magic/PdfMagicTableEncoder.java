package net.sf.anathema.character.reporting.pdf.rendering.boxes.magic;

import com.lowagie.text.pdf.PdfPTable;
import net.disy.commons.core.util.ObjectUtilities;
import net.sf.anathema.character.generic.magic.IMagicStats;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.content.stats.IStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicCostStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicDetailsStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicDurationStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicNameStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicSourceStatsGroup;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MagicTypeStatsGroup;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.stats.AbstractStatsTableEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.lib.resources.IResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdfMagicTableEncoder extends AbstractStatsTableEncoder<IMagicStats, ReportContent> {

  private final IResources resources;
  private List<IMagicStats> printStats = new ArrayList<IMagicStats>();
  private final boolean sectionHeaderLines;

  public PdfMagicTableEncoder(IResources resources, List<IMagicStats> printStats, boolean sectionHeaderLines) {
    super(sectionHeaderLines);
    this.resources = resources;
    this.printStats = printStats;
    this.sectionHeaderLines = sectionHeaderLines;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected IStatsGroup<IMagicStats>[] createStatsGroups(ReportContent content) {
    return new IStatsGroup[]{new MagicNameStatsGroup(resources), new MagicCostStatsGroup(resources), new MagicTypeStatsGroup(resources), new MagicDurationStatsGroup(resources), new MagicDetailsStatsGroup(resources), new MagicSourceStatsGroup(resources)};
  }

  @Override
  protected void encodeContent(SheetGraphics graphics, PdfPTable table, ReportContent content, Bounds bounds) {
    float heightLimit = bounds.height - 3;
    IStatsGroup<IMagicStats>[] groups = createStatsGroups(content);
    boolean encodeLine = true;
    String groupName = null;
    Collections.sort(printStats);
    for (IMagicStats stats : printStats.toArray(new IMagicStats[printStats.size()])) {
      String newGroupName = stats.getGroupName(resources);
      if (!ObjectUtilities.equals(groupName, newGroupName)) {
        groupName = newGroupName;
        encodeSectionLine(graphics, table, groupName);
        if (sectionHeaderLines) {
          encodeHeaderLine(graphics, table, groups);
        }
        encodeLine = table.getTotalHeight() < heightLimit;
        if (!encodeLine) {
          table.deleteLastRow();
          return;
        }
      }
      if (encodeLine) {
        encodeContentLine(graphics, table, groups, stats);
        encodeLine = table.getTotalHeight() < heightLimit;
        if (!encodeLine) {
          table.deleteLastRow();
          return;
        }
        printStats.remove(stats);
      }
    }
    while (encodeLine) {
      encodeContentLine(graphics, table, groups, null);
      encodeLine = table.getTotalHeight() < heightLimit;
    }
    table.deleteLastRow();
  }
}
