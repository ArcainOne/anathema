package net.sf.anathema.character.reporting.pdf.rendering.boxes.health;

import com.lowagie.text.Phrase;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.lib.resources.IResources;

public class Health2ndEditionTableEncoder extends AbstractHealthTableEncoder {

  public Health2ndEditionTableEncoder(IResources resources) {
    super(resources);
  }

  @Override
  protected final Phrase createIncapacitatedComment(SheetGraphics graphics) {
    return new Phrase(getResources().getString("Sheet.Movement.Comment.Mobility"), graphics.createCommentFont()); //$NON-NLS-1$
  }
}
