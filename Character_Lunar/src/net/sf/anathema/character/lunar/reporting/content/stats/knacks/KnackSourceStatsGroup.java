package net.sf.anathema.character.lunar.reporting.content.stats.knacks;

import net.sf.anathema.character.reporting.pdf.content.stats.AbstractTextStatsGroup;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;

public class KnackSourceStatsGroup extends AbstractTextStatsGroup<IKnackStats> {

  private final IResources resources;

  public KnackSourceStatsGroup(IResources resources) {
    this.resources = resources;
  }

  public void addContent(PdfPTable table, Font font, IKnackStats stats) {
    String text = stats == null ? null : stats.getSourceString(resources);
    table.addCell(createTextCell(font, text));
  }

  public Float[] getColumnWeights() {
    return new Float[] { 2.0f };
  }

  public String getTitle() {
    return resources.getString("Sheet.Magic.Source"); //$NON-NLS-1$
  }
}
