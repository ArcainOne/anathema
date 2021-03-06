package net.sf.anathema.character.reporting.pdf.rendering.page;

import com.itextpdf.text.DocumentException;
import net.sf.anathema.character.reporting.pdf.content.ReportSession;
import net.sf.anathema.character.reporting.pdf.layout.Sheet;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;

public interface PageEncoder {

  int FIRST_PAGE_CONTENT_HEIGHT = 755;

  void encode(Sheet sheet, SheetGraphics graphics, ReportSession session) throws DocumentException;
}
