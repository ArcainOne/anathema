package net.sf.anathema.character.reporting.sheet.common;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.reporting.util.Bounds;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;

public class NullPdfContentEncoder implements IPdfContentBoxEncoder {

  private final String headerKey;

  public NullPdfContentEncoder() {
    this("Null"); //$NON-NLS-1$
  }

  public NullPdfContentEncoder(String headerKey) {
    this.headerKey = headerKey;
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, IGenericDescription description, Bounds bounds) throws DocumentException {
    // Nothing to do
  }

  public String getHeaderKey(IGenericCharacter character, IGenericDescription description) {
    return headerKey;
  }
  
  public boolean hasContent(IGenericCharacter character)
  {
	  return true;
  }
}