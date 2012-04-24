package net.sf.anathema.character.reporting.pdf.content.stats;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import net.sf.anathema.character.generic.traits.IGenericTrait;
import net.sf.anathema.character.generic.util.IStats;
import net.sf.anathema.character.reporting.pdf.rendering.general.table.TableEncodingUtilities;
import net.sf.anathema.lib.resources.IResources;

public abstract class AbstractValueStatsGroup<T extends IStats> implements IStatsGroup<T> {

  private final String title;
  private final IResources resources;

  public AbstractValueStatsGroup(IResources resources, String resourceKey) {
    this.resources = resources;
    this.title = resources.getString(getHeaderResourceBase() + resourceKey);
  }

  protected abstract String getHeaderResourceBase();

  public final IResources getResources() {
    return resources;
  }

  @Override
  public Float[] getColumnWeights() {
    return TableEncodingUtilities.createStandardColumnWeights(getColumnCount());
  }

  @Override
  public final String getTitle() {
    return title;
  }

  protected final PdfPCell createFinalValueCell(Font font) {
    return createContentCellTable(BaseColor.BLACK, " ", font, 0.75f, true); //$NON-NLS-1$
  }

  protected final PdfPCell createFinalValueCell(Font font, Integer value) {
    return createFinalValueCell(font, value != null ? value.toString() : null);
  }

  protected final PdfPCell createFinalValueCell(Font font, String text) {
    String content = text != null ? text : " "; //$NON-NLS-1$
    return createContentCellTable(BaseColor.BLACK, content, font, 0.75f, text != null);
  }

  protected final PdfPCell createFinalValueCell(Font font, String text, int alignment) {
    String content = text != null ? text : " "; //$NON-NLS-1$
    return createContentCellTable(BaseColor.BLACK, content, font, 0.75f, alignment, text != null);
  }

  protected final PdfPCell createEmptyValueCell(Font font) {
    return createContentCellTable(BaseColor.GRAY, " ", font, 0.5f, true); //$NON-NLS-1$
  }

  protected final PdfPCell createEquipmentValueCell(Font font, Integer value) {
    String text = getStatsValueString(value);
    return createContentCellTable(BaseColor.GRAY, text, font, 0.5f, value != null);
  }

  private String getStatsValueString(Integer value) {
    if (value == null) {
      return " "; //$NON-NLS-1$
    }
    StringBuilder stringBuilder = new StringBuilder(value.toString());
    if (value == 0) {
      stringBuilder.insert(0, getZeroPrefix());
    }
    if (value > 0) {
      stringBuilder.insert(0, getPositivePrefix());
    }
    return stringBuilder.toString();
  }

  protected String getPositivePrefix() {
    return "+"; //$NON-NLS-1$
  }

  protected String getZeroPrefix() {
    return "+"; //$NON-NLS-1$
  }

  private PdfPCell createContentCellTable(BaseColor borderColor, String text, Font font, float borderWidth, boolean enabled) {
    return TableEncodingUtilities.createContentCellTable(borderColor, text, font, borderWidth, Rectangle.BOX, Element.ALIGN_RIGHT, enabled);
  }

  private PdfPCell createContentCellTable(BaseColor borderColor, String text, Font font, float borderWidth, int alignment, boolean enabled) {
    return TableEncodingUtilities.createContentCellTable(borderColor, text, font, borderWidth, Rectangle.BOX, alignment, enabled);
  }

  protected final int calculateFinalValue(final int weaponValue, IGenericTrait... traits) {
    int totalValue = weaponValue;
    for (IGenericTrait trait : traits) {
      totalValue += trait.getCurrentValue();
    }
    return totalValue;
  }
}
