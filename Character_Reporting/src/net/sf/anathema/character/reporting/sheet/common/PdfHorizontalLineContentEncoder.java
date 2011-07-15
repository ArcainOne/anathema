package net.sf.anathema.character.reporting.sheet.common;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.reporting.sheet.pageformat.IVoidStateFormatConstants;
import net.sf.anathema.character.reporting.sheet.util.HorizontalLineListEncoder;
import net.sf.anathema.character.reporting.util.Bounds;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;

public class PdfHorizontalLineContentEncoder implements IPdfContentBoxEncoder {

  private static final float LINE_HEIGHT = IVoidStateFormatConstants.LINE_HEIGHT - 2;
  private final int columnCount;
  private final String headerKey;

  public String getHeaderKey(IGenericCharacter character, IGenericDescription description) {
    return headerKey;
  }

  public PdfHorizontalLineContentEncoder(int columnCount, String headerKey) {
    this.columnCount = columnCount;
    this.headerKey = headerKey;
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, IGenericDescription description, Bounds bounds) throws DocumentException {
    float columnWidth = (bounds.width - (columnCount - 1) * IVoidStateFormatConstants.TEXT_PADDING) / columnCount;
    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
      float columnX = bounds.x + columnIndex * columnWidth + columnIndex * IVoidStateFormatConstants.TEXT_PADDING;
      Bounds columnBounds = new Bounds(columnX, bounds.y, columnWidth, bounds.height);
      new HorizontalLineListEncoder().encodeLines(directContent, columnBounds, LINE_HEIGHT);
    }
  }
  
  public boolean hasContent(IGenericCharacter character)
  {
	  return true;
  }
}