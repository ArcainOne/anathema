package net.sf.anathema.character.reporting.pdf.rendering.graphics.shape;

import com.lowagie.text.pdf.PdfContentByte;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Position;

import java.awt.*;

public abstract class AbstractShape implements Shape {
  protected PdfContentByte directContent;

  public AbstractShape(PdfContentByte directContent) {
    this.directContent = directContent;
  }

  protected PdfContentByte getDirectContent() {
    return directContent;
  }

  protected abstract void configureShape(Position lowerLeft);

  @Override
  public void encodeOutlined(Position lowerLeft) {
    configureDirectContent();
    configureShape(lowerLeft);
    getDirectContent().stroke();
  }

  @Override
  public void encodeFilled(Position lowerLeft) {
    configureDirectContent();
    configureShape(lowerLeft);
    getDirectContent().fillStroke();
  }

  private void configureDirectContent() {
    directContent.setColorFill(Color.BLACK);
    directContent.setLineWidth(0.8f);
  }
}
