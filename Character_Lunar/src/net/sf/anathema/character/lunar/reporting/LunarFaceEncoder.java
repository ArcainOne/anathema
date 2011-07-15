package net.sf.anathema.character.lunar.reporting;

import java.awt.Color;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.character.lunar.renown.RenownTemplate;
import net.sf.anathema.character.lunar.renown.presenter.IRenownModel;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.util.AbstractTableEncoder;
import net.sf.anathema.character.reporting.sheet.util.TableEncodingUtilities;
import net.sf.anathema.character.reporting.util.Bounds;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class LunarFaceEncoder extends AbstractTableEncoder implements IPdfContentBoxEncoder {

  private final Font font;
  private final IResources resources;

  public LunarFaceEncoder(BaseFont baseFont, IResources resources) {
    this.resources = resources;
    this.font = TableEncodingUtilities.createFont(baseFont);
  }

  public String getHeaderKey(IGenericCharacter character, IGenericDescription description) {
    return "Lunar.Face"; //$NON-NLS-1$
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, IGenericDescription description, Bounds bounds) throws DocumentException {
    encodeTable(directContent, character, bounds);
  }

  @Override
  protected PdfPTable createTable(PdfContentByte directContent, IGenericCharacter character, Bounds bounds)
      throws DocumentException {
    PdfPTable table = new PdfPTable(new float[] { 6, 0.2f, 2.5f });
    table.setTotalWidth(bounds.width);
    float lineOffset = encodeContent(table, character);
    float delimitingLineYPosition = bounds.getMaxY() - lineOffset - 1;
    drawDelimiter(directContent, bounds, delimitingLineYPosition);
    return table;
  }

  protected float encodeContent(PdfPTable table, IGenericCharacter character) {
    IRenownModel model = (IRenownModel) character.getAdditionalModel(RenownTemplate.TEMPLATE_ID);
    ITrait[] traits = model.getAllTraits();
    for (ITrait trait : traits) {
      encodeRenownTraitLine(table, trait);
    }
    float height = table.getTotalHeight();
    int currentRank = model.getFace().getCurrentValue();
    String totalString = resources.getString("Lunar.Renown.FaceLabel") + " " //$NON-NLS-1$//$NON-NLS-2$
        + currentRank
        + ": " //$NON-NLS-1$
        + resources.getString("Lunar.Renown.Rank." + currentRank) //$NON-NLS-1$
        + " / " //$NON-NLS-1$
        + resources.getString("Lunar.Renown.RenownTotal"); //$NON-NLS-1$
    encodeRenownLine(table, totalString, model.calculateTotalRenown());
    return height;
  }

  private void drawDelimiter(PdfContentByte directContent, Bounds bounds, float delimitingLineYPosition) {
    directContent.moveTo(bounds.getMinX() + 3, delimitingLineYPosition);
    directContent.lineTo(bounds.getMaxX() - 3, delimitingLineYPosition);
    directContent.setColorStroke(Color.GRAY);
    directContent.setLineWidth(0.5f);
    directContent.stroke();
    directContent.setColorStroke(Color.BLACK);
  }

  private void encodeRenownTraitLine(PdfPTable table, ITrait trait) {
    String text = resources.getString("Lunar.Renown." + trait.getType().getId()); //$NON-NLS-1$
    encodeRenownLine(table, text, trait.getCurrentValue());
  }

  private void encodeRenownLine(PdfPTable table, String text, int value) {
    table.addCell(createTextCell(text, Element.ALIGN_LEFT));
    table.addCell(createSpaceCell());
    table.addCell(createTextCell(String.valueOf(value), Element.ALIGN_RIGHT));
  }

  private final PdfPCell createTextCell(String text, int alignment) {
    PdfPCell cell = TableEncodingUtilities.createContentCellTable(
        Color.BLACK,
        text,
        font,
        0.5f,
        Rectangle.NO_BORDER,
        alignment);
    cell.setPadding(0);
    return cell;
  }

  private PdfPCell createSpaceCell() {
    PdfPCell cell = new PdfPCell(new Phrase("", font)); //$NON-NLS-1$
    cell.setBorder(Rectangle.NO_BORDER);
    return cell;
  }
  
  public boolean hasContent(IGenericCharacter character)
  {
	  return true;
  }
}