package net.sf.anathema.character.reporting.pdf.rendering.boxes.magic;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import net.disy.commons.core.util.ArrayUtilities;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.content.magic.CharmPrintNameTransformer;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Position;
import net.sf.anathema.character.reporting.pdf.rendering.general.HorizontalLineEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.PdfBoxEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SimpleColumn;
import net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants;
import net.sf.anathema.lib.lang.AnathemaStringUtilities;
import net.sf.anathema.lib.resources.IResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants.LINE_HEIGHT;

public class ComboEncoder {

  private final IResources resources;
  private final PdfBoxEncoder boxEncoder;

  public ComboEncoder(IResources resources) {
    this.resources = resources;
    this.boxEncoder = new PdfBoxEncoder();
  }

  public float encodeCombos(SheetGraphics graphics, ReportContent content, Bounds maxBounds) throws DocumentException {
    List<IGenericCombo> combos = new ArrayList<IGenericCombo>(Arrays.asList(content.getCharacter().getCombos()));
    return encodeCombos(graphics, combos, maxBounds, false);
  }

  public float encodeCombos(SheetGraphics graphics, List<IGenericCombo> combos, Bounds maxBounds, boolean overflow) throws DocumentException {
    if (combos.isEmpty()) {
      return 0;
    }
    Bounds contentBounds = boxEncoder.calculateContentBounds(maxBounds);
    SimpleColumn column = graphics.createSimpleColumn(contentBounds).withLeading(LINE_HEIGHT).get();
    addCombos(graphics, column, combos);

    float yPosition = column.getYLine();
    Bounds actualBoxBounds = calculateActualBoxBounds(maxBounds, yPosition);
    String headerString;
    if (overflow) {
      headerString = resources.getString("Sheet.Header.CombosOverflow"); //$NON-NLS-1$
    } else {
      headerString = resources.getString("Sheet.Header.Combos"); //$NON-NLS-1$
    }
    boxEncoder.encodeBox(graphics, actualBoxBounds, headerString);
    return actualBoxBounds.getHeight();
  }

  public float encodeFixedCombos(SheetGraphics graphics, List<IGenericCombo> combos, Bounds bounds) throws DocumentException {
    Bounds contentBounds = boxEncoder.calculateContentBounds(bounds);
    SimpleColumn column = graphics.createSimpleColumn(contentBounds).withLeading(LINE_HEIGHT).get();
    addCombos(graphics, column, combos);

    float yPosition = column.getYLine();
    int remainingLines = (int) ((yPosition - contentBounds.getMinY()) / LINE_HEIGHT);
    Position lineStartPosition = new Position(contentBounds.getMinX(), yPosition - LINE_HEIGHT);
    new HorizontalLineEncoder().encodeLines(graphics, lineStartPosition, contentBounds.getMinX(), contentBounds.getMaxX(), LINE_HEIGHT, remainingLines);

    String headerString = resources.getString("Sheet.Header.Combos"); //$NON-NLS-1$
    boxEncoder.encodeBox(graphics, bounds, headerString);
    return bounds.getHeight();
  }

  private void addCombos(SheetGraphics graphics, SimpleColumn columnText, List<IGenericCombo> combos) throws DocumentException {
    while (!combos.isEmpty()) {
      Phrase comboPhrase = createComboPhrase(graphics, combos.get(0));

      float yLine = columnText.getYLine();
      columnText.addText(comboPhrase);
      int status = columnText.simulate();
      columnText.setYLine(yLine);
      columnText.setText(comboPhrase);
      if (SimpleColumn.hasMoreText(status)) {
        break;
      } else {
        columnText.encode();
        combos.remove(0);
      }
    }
  }

  private Phrase createComboPhrase(SheetGraphics graphics, IGenericCombo combo) {
    Phrase phrase = new Phrase();

    String printName = combo.getName() == null ? "???" : combo.getName(); //$NON-NLS-1$
    phrase.add(new Chunk(printName + ": ", graphics.createBoldFont())); //$NON-NLS-1$

    String charmString = getCharmString(combo);
    phrase.add(new Chunk(charmString, graphics.createTextFont()));

    return phrase;
  }

  private Bounds calculateActualBoxBounds(Bounds restOfPage, float textEndY) {
    float boxY = textEndY - IVoidStateFormatConstants.TEXT_PADDING;
    return new Bounds(restOfPage.x, boxY, restOfPage.width, restOfPage.getMaxY() - boxY);
  }

  private String getCharmString(IGenericCombo combo) {
    CharmPrintNameTransformer transformer = new CharmPrintNameTransformer(resources);
    String[] charmNames = ArrayUtilities.transform(combo.getCharms(), String.class, transformer);
    return AnathemaStringUtilities.concat(charmNames, ", "); //$NON-NLS-1$
  }
}
