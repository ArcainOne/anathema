package net.sf.anathema.character.reporting.sheet.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.groups.IIdentifiedTraitTypeGroup;
import net.sf.anathema.character.reporting.sheet.util.PdfTraitEncoder;
import net.sf.anathema.character.reporting.util.Position;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class SpecialtiesEncoder extends AbstractNamedTraitEncoder implements INamedTraitEncoder {
  
  private final int specialtyCount;

  public SpecialtiesEncoder(IResources resources, BaseFont baseFont, PdfTraitEncoder encoder, int specialtyCount) {
    super(resources, baseFont, encoder);
    this.specialtyCount = specialtyCount;
  }

  public float encode(PdfContentByte directContent, IGenericCharacter character, Position position, float width, float height) {
    String title = getResources().getString("Sheet.AbilitySubHeader.Specialties"); //$NON-NLS-1$
    List<IValuedTraitReference> references = new ArrayList<IValuedTraitReference>();
    for (IIdentifiedTraitTypeGroup group : character.getAbilityTypeGroups()) {
      for (ITraitType traitType : group.getAllGroupTypes()) {
        Collections.addAll(references, getTraitReferences(character.getSpecialties(traitType), traitType));
      }
    }
    IValuedTraitReference[] specialties = references.toArray(new IValuedTraitReference[references.size()]);
    if (specialtyCount > 0) {
      return _drawNamedTraitSection(directContent, title, specialties, position, width, specialtyCount, 3);
    }
    else {
      return drawNamedTraitSection(directContent, title, specialties, position, width, height, 3);
    }
  }
}