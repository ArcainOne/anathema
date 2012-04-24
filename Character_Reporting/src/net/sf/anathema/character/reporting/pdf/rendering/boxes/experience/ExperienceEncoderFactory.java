package net.sf.anathema.character.reporting.pdf.rendering.boxes.experience;

import net.sf.anathema.character.reporting.pdf.content.BasicContent;
import net.sf.anathema.character.reporting.pdf.rendering.EncoderIds;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.AbstractEncoderFactory;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.RegisteredEncoderFactory;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.lib.resources.IResources;

@RegisteredEncoderFactory
public class ExperienceEncoderFactory extends AbstractEncoderFactory {

  public ExperienceEncoderFactory() {
    super(EncoderIds.EXPERIENCE);
  }

  @Override
  public ContentEncoder create(IResources resources, BasicContent content) {
    return new ExperienceEncoder();
  }

  @Override
  public boolean supports(BasicContent content) {
    return true;
  }
}
