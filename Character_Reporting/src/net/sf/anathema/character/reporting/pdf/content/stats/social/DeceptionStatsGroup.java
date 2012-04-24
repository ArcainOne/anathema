package net.sf.anathema.character.reporting.pdf.content.stats.social;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import net.sf.anathema.character.generic.social.ISocialCombatStats;
import net.sf.anathema.lib.resources.IResources;

public class DeceptionStatsGroup extends AbstractSocialCombatsValueStatsGroup {

  public DeceptionStatsGroup(IResources resources) {
    super(resources, "Deception"); //$NON-NLS-1$
  }

  @Override
  public void addContent(PdfPTable table, Font font, ISocialCombatStats stats) {
    if (stats == null) {
      table.addCell(createFinalValueCell(font));
      table.addCell(createFinalValueCell(font));
    } else {
      table.addCell(createFinalValueCell(font, stats.getDeceptionAttackValue()));
      table.addCell(createFinalValueCell(font, stats.getDeceptionMDV()));
    }
  }

  @Override
  public int getColumnCount() {
    return 2;
  }
}
