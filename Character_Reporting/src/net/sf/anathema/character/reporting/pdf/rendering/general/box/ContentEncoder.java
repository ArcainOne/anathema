package net.sf.anathema.character.reporting.pdf.rendering.general.box;

import com.lowagie.text.DocumentException;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;

public interface ContentEncoder {

  void encode(SheetGraphics graphics, ReportContent reportContent, Bounds bounds) throws DocumentException;

  boolean hasContent(ReportContent content);

  String getHeader(ReportContent content);
}
