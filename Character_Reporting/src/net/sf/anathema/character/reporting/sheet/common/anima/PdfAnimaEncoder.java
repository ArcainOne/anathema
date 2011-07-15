package net.sf.anathema.character.reporting.sheet.common.anima;

import java.awt.Color;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfEncodingUtilities;
import net.sf.anathema.character.reporting.sheet.util.AbstractPdfEncoder;
import net.sf.anathema.character.reporting.sheet.util.IPdfTableEncoder;
import net.sf.anathema.character.reporting.sheet.util.PdfLineEncodingUtilities;
import net.sf.anathema.character.reporting.sheet.util.PdfTextEncodingUtilities;
import net.sf.anathema.character.reporting.util.Bounds;
import net.sf.anathema.character.reporting.util.Position;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class PdfAnimaEncoder extends AbstractPdfEncoder implements IPdfContentBoxEncoder {

  private final int fontSize;
  private final float lineHeight;
  private final BaseFont baseFont;
  private final IResources resources;
  private final BaseFont symbolBaseFont;
  private final Chunk symbolChunk;
  private final IPdfTableEncoder tableEncoder;
  private final int animaPowerCount;

  public PdfAnimaEncoder(
      IResources resources,
      BaseFont baseFont,
      BaseFont symbolBaseFont,
      int fontSize,
      int animaPowerCount,
      IPdfTableEncoder encoder) {
    this.resources = resources;
    this.baseFont = baseFont;
    this.symbolBaseFont = symbolBaseFont;
    this.fontSize = fontSize;
    this.animaPowerCount = animaPowerCount;
    this.lineHeight = fontSize * 1.5f;
    this.tableEncoder = encoder;
    this.symbolChunk = PdfEncodingUtilities.createCaretSymbolChunk(symbolBaseFont);
  }

  public String getHeaderKey(IGenericCharacter character, IGenericDescription description) {
    return "Anima"; //$NON-NLS-1$
  }

  @Override
  protected BaseFont getBaseFont() {
    return baseFont;
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, IGenericDescription description, Bounds bounds) throws DocumentException {
    float halfWidth = bounds.getHeight() / 2;
    Bounds animaPowerBounds = new Bounds(bounds.getMinX(), bounds.getCenterY(), bounds.getWidth(), halfWidth);
    Position lineStartPosition = encodeAnimaPowers(directContent, character, animaPowerBounds);
    if (lineStartPosition != null) {
      encodeLines(directContent, bounds, lineStartPosition);
    }
    Bounds animaTableBounds = new Bounds(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), halfWidth);
    tableEncoder.encodeTable(directContent, character, animaTableBounds);
  }

  private void encodeLines(PdfContentByte directContent, Bounds bounds, Position lineStartPosition) {
    float minX = bounds.getMinX();
    float maxX = bounds.getMaxX();
    PdfLineEncodingUtilities.encodeHorizontalLines(
        directContent,
        lineStartPosition,
        minX,
        maxX,
        lineHeight,
        6 - animaPowerCount);
  }

  private Position encodeAnimaPowers(PdfContentByte directContent, IGenericCharacter character, Bounds bounds)
      throws DocumentException {
    Phrase phrase = new Phrase("", new Font(baseFont, fontSize, Font.NORMAL, Color.BLACK)); //$NON-NLS-1$
    addAnimaPowerText(character, phrase);
    String casteResourceKey = "Sheet.AnimaPower." + character.getCasteType().getId() + "." + character.getRules().getEdition().getId(); //$NON-NLS-1$ //$NON-NLS-2$
    boolean isCastePowerDefined = resources.supportsKey(casteResourceKey);
    if (isCastePowerDefined) {
      phrase.add(symbolChunk);
      phrase.add(resources.getString(casteResourceKey));
      PdfTextEncodingUtilities.encodeText(directContent, phrase, bounds, lineHeight).getYLine();
      return null;
    }
    phrase.add(symbolChunk);
    float yPosition = PdfTextEncodingUtilities.encodeText(directContent, phrase, bounds, lineHeight).getYLine();
    return new Position((bounds.getMinX() + PdfEncodingUtilities.getCaretSymbolWidth(symbolBaseFont)), yPosition);
  }

  private void addAnimaPowerText(IGenericCharacter character, Phrase phrase) {
    ICharacterType characterType = character.getTemplate().getTemplateType().getCharacterType();
    String resourceBase = "Sheet.AnimaPower." + characterType.getId() + "."; //$NON-NLS-1$ //$NON-NLS-2$
    String baseWithEdition = resourceBase + character.getRules().getEdition().getId() + ".";
    for (int power = 0; power < animaPowerCount; power++) {
      phrase.add(symbolChunk);
      String resource;
      if (resources.supportsKey(baseWithEdition + AnimaUtils.resourceIds[power])) {
        resource = resources.getString(baseWithEdition + AnimaUtils.resourceIds[power]);
      }
      else {
        resource = resources.getString(resourceBase + AnimaUtils.resourceIds[power]);
      }
      phrase.add(resource + "\n"); //$NON-NLS-1$
    }
  }
  
  public boolean hasContent(IGenericCharacter character)
  {
	  return true;
  }
}