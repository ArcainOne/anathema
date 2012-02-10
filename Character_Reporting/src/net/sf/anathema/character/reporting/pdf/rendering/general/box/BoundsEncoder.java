package net.sf.anathema.character.reporting.pdf.rendering.general.box;

import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;

public interface BoundsEncoder {

  public static final int HEADER_HEIGHT = 12;
  public static final int ARC_SPACE = HEADER_HEIGHT / 2;
  public static final int ARC_SIZE = 2 * ARC_SPACE;

  public void encodeBoxBounds(SheetGraphics graphics, Bounds bounds);
}
