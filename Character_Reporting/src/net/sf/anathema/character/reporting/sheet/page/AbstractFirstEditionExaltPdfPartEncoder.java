package net.sf.anathema.character.reporting.sheet.page;

import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.reporting.sheet.PdfEncodingRegistry;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfDotsEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfEssenceEncoder;
import net.sf.anathema.lib.resources.IResources;

public abstract class AbstractFirstEditionExaltPdfPartEncoder extends AbstractFirstEditionPartEncoder {

  private final int essenceMax;

  public AbstractFirstEditionExaltPdfPartEncoder(IResources resources, PdfEncodingRegistry registry, int essenceMax) {
    super(resources, registry.getBaseFont(), registry.getSymbolBaseFont());
    this.essenceMax = essenceMax;
  }

  public IPdfContentBoxEncoder getEssenceEncoder() {
    return new PdfEssenceEncoder(getBaseFont(), getResources(), essenceMax);
  }

  public IPdfContentBoxEncoder getDotsEncoder(OtherTraitType trait, int traitMax, String traitHeaderKey) {
    return new PdfDotsEncoder(getBaseFont(), getResources(), trait, traitMax, traitHeaderKey);
  }
  
  public IPdfContentBoxEncoder getOverdriveEncoder()
  {
	return null;
  }

  protected int getEssenceMax() {
    return essenceMax;
  }
  
  public boolean hasMagicPage() {
	return true;
  }

  protected final int getFontSize() {
    return FONT_SIZE;
  }
}