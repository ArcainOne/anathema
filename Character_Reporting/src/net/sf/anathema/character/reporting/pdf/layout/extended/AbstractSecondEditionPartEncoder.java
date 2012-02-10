package net.sf.anathema.character.reporting.pdf.layout.extended;

import com.lowagie.text.pdf.BaseFont;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.essence.ExtendedEssenceBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.health.Extended2ndEditionHealthEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.health.Extended2ndEditionMovementEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.DotBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.IVariableContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants;
import net.sf.anathema.character.reporting.pdf.rendering.page.PageConfiguration;
import net.sf.anathema.character.reporting.pdf.rendering.page.PageEncoder;
import net.sf.anathema.lib.resources.IResources;

public abstract class AbstractSecondEditionPartEncoder implements IExtendedPartEncoder {

  private final IResources resources;
  private final BaseFont baseFont;
  private final int essenceMax;

  public AbstractSecondEditionPartEncoder(IResources resources, BaseFont baseFont, int essenceMax) {
    this.resources = resources;
    this.baseFont = baseFont;
    this.essenceMax = essenceMax;
  }

  public final IResources getResources() {
    return resources;
  }

  protected int getEssenceMax() {
    return essenceMax;
  }

  protected int getFontSize() {
    return IVoidStateFormatConstants.SMALLER_FONT_SIZE;
  }

  @Override
  public ContentEncoder getEssenceEncoder() {
    return new ExtendedEssenceBoxContentEncoder();
  }

  @Override
  public ContentEncoder getDotsEncoder(OtherTraitType trait, int traitMax, String traitHeaderKey) {
    return new DotBoxContentEncoder(trait, traitMax, resources, traitHeaderKey);
  }

  @Override
  public ContentEncoder getHealthEncoder() {
    return new Extended2ndEditionHealthEncoder(resources);
  }

  @Override
  public ContentEncoder getMovementEncoder() {
    return new Extended2ndEditionMovementEncoder(resources, baseFont);
  }

  @Override
  public IVariableContentEncoder[] getAdditionalFirstPageEncoders() {
    return new IVariableContentEncoder[0];
  }

  @Override
  public PageEncoder[] getAdditionalPages(PageConfiguration configuration) {
    return new PageEncoder[0];
  }
}
