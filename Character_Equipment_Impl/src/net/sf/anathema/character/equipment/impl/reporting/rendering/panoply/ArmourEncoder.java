package net.sf.anathema.character.equipment.impl.reporting.rendering.panoply;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.table.ITableEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.lib.resources.IResources;

import java.awt.*;

public class ArmourEncoder implements ContentEncoder {

  private IResources resources;
  private final ITableEncoder encoder;

  public ArmourEncoder(IResources resources, ITableEncoder encoder) {
    this.resources = resources;
    this.encoder = encoder;
  }

  @Override
  public String getHeader(ReportContent content) {
    return resources.getString("Sheet.Header.ArmourSoak");
  }

  public void encode(SheetGraphics graphics, ReportContent content, Bounds bounds) throws DocumentException {
    float tableHeight = encoder.encodeTable(graphics, content, bounds);
    float remainingHeight = bounds.getHeight() - tableHeight;
    int padding = 1;
    float delimitingLineYPosition = bounds.getMinY() + remainingHeight - padding;
    drawDelimiter(graphics.getDirectContent(), bounds, delimitingLineYPosition);
    Bounds shieldBounds = new Bounds(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), remainingHeight - 2 * padding);
    new ShieldTableEncoder().encodeTable(graphics, content, shieldBounds);
  }

  private void drawDelimiter(PdfContentByte directContent, Bounds bounds, float delimitingLineYPosition) {
    directContent.moveTo(bounds.getMinX() + 3, delimitingLineYPosition);
    directContent.lineTo(bounds.getMaxX() - 3, delimitingLineYPosition);
    directContent.setColorStroke(Color.GRAY);
    directContent.setLineWidth(0.75f);
    directContent.stroke();
    directContent.setColorStroke(Color.BLACK);
  }

  public boolean hasContent(ReportContent content) {
    return true;
  }
}
