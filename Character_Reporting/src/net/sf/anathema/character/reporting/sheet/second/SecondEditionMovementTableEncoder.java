package net.sf.anathema.character.reporting.sheet.second;

import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.health.HealthLevelType;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.character.reporting.sheet.common.movement.AbstractMovementTableEncoder;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;

public class SecondEditionMovementTableEncoder extends AbstractMovementTableEncoder {

  public SecondEditionMovementTableEncoder(IResources resources, BaseFont baseFont) {
    super(resources, baseFont);
  }

  @Override
  protected final Float[] getMovementColumns() {
    return new Float[] { 0.7f, 0.6f, PADDING, 1f, PADDING, 1f, PADDING, 1f, 1f };
  }

  @Override
  protected final Phrase createIncapacitatedComment() {
    return new Phrase(getResources().getString("Sheet.Movement.Comment.Mobility"), getCommentFont()); //$NON-NLS-1$
  }

  @Override
  protected final void addMovementHeader(PdfPTable table) {
    table.addCell(createHeaderCell(getResources().getString("Sheet.Health.Levels"), 3)); //$NON-NLS-1$
    table.addCell(createHeaderCell(getResources().getString("Sheet.Movement.Move"), 2)); //$NON-NLS-1$
    table.addCell(createHeaderCell(getResources().getString("Sheet.Movement.Dash"), 2)); //$NON-NLS-1$
    table.addCell(createHeaderCell(getResources().getString("Sheet.Movement.Jump"), 3)); //$NON-NLS-1$
  }

  @Override
  protected final void addMovementCells(
      PdfPTable table,
      HealthLevelType level,
      int painTolerance,
      IGenericTraitCollection collection) {
    addHealthPenaltyCells(table, level, painTolerance);
    addSpaceCells(table, 1);
    
    int penalty = getPenalty(level, painTolerance);
    int dexValue = collection.getTrait(AttributeType.Dexterity).getCurrentValue();
    int moveValue = dexValue + penalty;
    table.addCell(createMovementCell(moveValue, 1));
    addSpaceCells(table, 1);
    table.addCell(createMovementCell(moveValue + 6, 2));
    int verticalJump = collection.getTrait(AttributeType.Strength).getCurrentValue()
        + collection.getTrait(AbilityType.Athletics).getCurrentValue()
        + penalty;
    addSpaceCells(table, 1);
    table.addCell(createMovementCell(verticalJump * 2, 0));
    table.addCell(createMovementCell(verticalJump, 0));
  }
}