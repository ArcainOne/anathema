package net.sf.anathema.character.sidereal.reporting;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.util.Bounds;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class AstrologyInfoEncoder implements IPdfContentBoxEncoder {

  private final IResources resources;
  private final BaseFont basefont;

  public AstrologyInfoEncoder(BaseFont baseFont, IResources resources) {
    this.resources = resources;
    this.basefont = baseFont;
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, IGenericDescription description, Bounds bounds) throws DocumentException {
    new AstrologyTableEncoder(resources, basefont).encodeTable(directContent, character, bounds);

  }

  public String getHeaderKey(IGenericCharacter character, IGenericDescription description) {
    return "Sidereal.Astrology"; //$NON-NLS-1$
  }
  
  public boolean hasContent(IGenericCharacter character)
  {
	  return true;
  }
}