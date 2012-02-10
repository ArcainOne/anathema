package net.sf.anathema.character.reporting.pdf.rendering.boxes.magic;

import com.lowagie.text.DocumentException;
import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.impl.magic.CharmUtilities;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IMagicStats;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.CharmStats;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.MultipleEffectCharmStats;
import net.sf.anathema.character.reporting.pdf.content.stats.magic.SpellStats;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.table.ITableEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;

import java.util.ArrayList;
import java.util.List;

public class MagicEncoder implements ContentEncoder {
  static IIdentificate KNACK = new Identificate("Knack");
  private IResources resources;

  public static List<IMagicStats> collectPrintCharms(ReportContent content) {
    return collectPrintMagic(content.getCharacter(), false, true);
  }

  public static List<IMagicStats> collectPrintMagic(ReportContent content) {
    return collectPrintMagic(content.getCharacter(), true, true);
  }

  public static List<IMagicStats> collectPrintSpells(ReportContent content) {
    return collectPrintMagic(content.getCharacter(), true, false);
  }

  private static List<IMagicStats> collectPrintMagic(final IGenericCharacter character, final boolean includeSpells, final boolean includeCharms) {
    final List<IMagicStats> printStats = new ArrayList<IMagicStats>();
    if (includeCharms) {
      for (IMagicStats stats : character.getGenericCharmStats()) {
        printStats.add(stats);
      }
    }

    IMagicVisitor statsCollector = new IMagicVisitor() {
      @Override
      public void visitCharm(ICharm charm) {
        if (!includeCharms) {
          return;
        }
        if (CharmUtilities.isGenericCharmFor(charm, character)) {
          return;
        }
        if (charm.hasAttribute(KNACK)) {
          return;
        }

        if (character.isMultipleEffectCharm(charm)) {
          String[] effects = character.getLearnedEffects(charm);
          for (String effect : effects) {
            printStats.add(new MultipleEffectCharmStats(charm, effect));
          }
        } else {
          printStats.add(new CharmStats(charm, character));
        }
      }

      @Override
      public void visitSpell(ISpell spell) {
        if (includeSpells) {
          printStats.add(new SpellStats(spell, character.getRules().getEdition()));
        }
      }
    };
    for (IMagic magic : character.getAllLearnedMagic()) {
      magic.accept(statsCollector);
    }
    return printStats;
  }

  private final PdfMagicTableEncoder tableEncoder;
  private final List<ITableEncoder> additionalTables;
  private final String headerKey;

  public MagicEncoder(IResources resources, List<IMagicStats> printMagic) {
    this(resources, printMagic, new ArrayList<ITableEncoder>(), false, "Charms"); //$NON-NLS-1$
  }

  public MagicEncoder(IResources resources, List<IMagicStats> printMagic, List<ITableEncoder> additionalTables, boolean sectionHeaderLines, String headerKey) {
    this.resources = resources;
    this.tableEncoder = new PdfMagicTableEncoder(resources, printMagic, sectionHeaderLines);
    this.additionalTables = additionalTables;
    this.headerKey = headerKey;
  }

  @Override
  public String getHeader(ReportContent content) {
    return resources.getString("Sheet.Header." + headerKey);
  }

  @Override
  public void encode(SheetGraphics graphics, ReportContent reportContent, Bounds bounds) throws DocumentException {
    float top = bounds.getMinY();
    for (ITableEncoder additionalTable : additionalTables) {
      if (additionalTable.hasContent(reportContent)) {
        Bounds tableBounds = new Bounds(bounds.getMinX(), top, bounds.getWidth(), bounds.getMaxY() - top);
        float tableHeight = additionalTable.encodeTable(graphics, reportContent, tableBounds);
        top += tableHeight + IVoidStateFormatConstants.PADDING;
      }
    }

    Bounds remainingBounds = new Bounds(bounds.getMinX(), top, bounds.getWidth(), bounds.getMaxY() - top);
    tableEncoder.encodeTable(graphics, reportContent, remainingBounds);
  }

  @Override
  public boolean hasContent(ReportContent content) {
    return true;
  }
}
