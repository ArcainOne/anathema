package net.sf.anathema.character.reporting.sheet.page;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.reporting.sheet.PdfEncodingRegistry;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.common.IPdfVariableContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfAbilitiesEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfAttributesEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfBackgroundEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfExperienceEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfSpecialtiesEncoder;
import net.sf.anathema.character.reporting.sheet.common.PdfVirtueEncoder;
import net.sf.anathema.character.reporting.sheet.pageformat.IVoidStateFormatConstants;
import net.sf.anathema.character.reporting.sheet.pageformat.PdfPageConfiguration;
import net.sf.anathema.character.reporting.sheet.second.NewSecondEditionPersonalInfoEncoder;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;

public class NewPdfFirstPageEncoder extends AbstractPdfPageEncoder {
  private final int essenceMax;

  public NewPdfFirstPageEncoder(IPdfPartEncoder partEncoder,
                                PdfEncodingRegistry registry,
                                IResources resources, int essenceMax,
                                PdfPageConfiguration pageConfiguration) {
    super(partEncoder, registry, resources, pageConfiguration);
    this.essenceMax = essenceMax;
  }

  public void encode(Document document, PdfContentByte directContent,
                     IGenericCharacter character,
                     IGenericDescription description) throws DocumentException {
    ICharacterType characterType = character.getTemplate().getTemplateType().getCharacterType();

    float distanceFromTop = 0;
    float firstRowHeight = encodePersonalInfo(directContent, character,
                                              description, distanceFromTop, 60);
    distanceFromTop += calculateBoxIncrement(firstRowHeight);

    // First column - top-down
    float firstDistanceFromTop = distanceFromTop;
    float attributeHeight = encodeAttributes(directContent, character,
                                             description, firstDistanceFromTop, 117);
    firstDistanceFromTop += calculateBoxIncrement(attributeHeight);
    float abilityHeight = encodeAbilities(directContent, character,
                                          description, firstDistanceFromTop, 415);
    firstDistanceFromTop += calculateBoxIncrement(abilityHeight);
    // First column - fill in (bottom-up) with specialties
    float specialtyHeight = CONTENT_HEIGHT - firstDistanceFromTop;
    encodeSpecialties(directContent, character, description,
                      firstDistanceFromTop, specialtyHeight);

    // Second column - top-down
    float secondDistanceFromTop = distanceFromTop;
    float virtueHeight = encodeVirtues(directContent, character,
                                       description, secondDistanceFromTop, 72);
    float virtueIncrement = calculateBoxIncrement(virtueHeight);
    secondDistanceFromTop += virtueIncrement;
    if (characterType.isExaltType()) {
      float actualGreatCurseHeight = encodeGreatCurse(directContent, character,
                                                      description, secondDistanceFromTop, 85);
      secondDistanceFromTop += calculateBoxIncrement(actualGreatCurseHeight);
    }
    // Second column - fill in (bottom-up) with mutations, merits & flaws, intimacies
    float secondBottom = CONTENT_HEIGHT - calculateBoxIncrement(specialtyHeight);
    float reservedHeight = (secondBottom - secondDistanceFromTop) / 4f - IVoidStateFormatConstants.PADDING;
    if (hasMutations(character)) {
      float mutationsHeight = encodeMutations(directContent, character, description,
                                              secondBottom - reservedHeight, reservedHeight);
      secondBottom -= calculateBoxIncrement(mutationsHeight);
    }
    if (hasMeritsAndFlaws(character)) {
      float meritsHeight = encodeMeritsAndFlaws(directContent, character, description,
                                                secondBottom - reservedHeight, reservedHeight);
      secondBottom -= calculateBoxIncrement(meritsHeight);
    }
    encodeIntimacies(directContent, character,
                     description, secondDistanceFromTop, secondBottom - secondDistanceFromTop);

    // Third column - top-down (lining up with virtues)
    float thirdDistanceFromTop = distanceFromTop;
    float dotsHeight = 30;
    encodeEssenceDots(directContent, character,
                      description, thirdDistanceFromTop, dotsHeight);
    encodeWillpowerDots(directContent, character,
                        description, thirdDistanceFromTop + virtueHeight - dotsHeight, dotsHeight);
    thirdDistanceFromTop += virtueIncrement;
    // Third column - bottom-up
    float thirdBottom = CONTENT_HEIGHT;
    float experienceHeight = encodeExperience(directContent, character,
                                              description, thirdBottom - 30, 30);
    float experienceIncrement = calculateBoxIncrement(experienceHeight);
    thirdBottom -= experienceIncrement;
    float languageHeight = specialtyHeight - experienceIncrement;
    languageHeight = encodeLinguistics(directContent, character,
                                       description, thirdBottom - languageHeight, languageHeight);
    thirdBottom -= calculateBoxIncrement(languageHeight);
    float additionalIncrement = encodeAdditional(directContent, character, description, thirdDistanceFromTop, thirdBottom);
    thirdBottom -= additionalIncrement;
    // Third column - fill in (bottom-up) with backgrounds
    encodeBackgrounds(directContent, character, description,
                      thirdDistanceFromTop, thirdBottom - thirdDistanceFromTop);

    encodeCopyright(directContent);
  }

  private float encodeEssenceDots(PdfContentByte directContent,
                                  IGenericCharacter character, IGenericDescription description,
                                  float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getPartEncoder().getDotsEncoder(OtherTraitType.Essence,
                                                          EssenceTemplate.SYSTEM_ESSENCE_MAX,
                                                          "Essence"),
                          3, 1, distanceFromTop, height);
  }

  private float encodePersonalInfo(PdfContentByte directContent,
                                   IGenericCharacter character, IGenericDescription description,
                                   float distanceFromTop, float maxHeight)
      throws DocumentException {
    return encodeVariableBox(directContent, character, description,
                             new NewSecondEditionPersonalInfoEncoder(getBaseFont(),
                                                                     getResources()),
                             1, 3, distanceFromTop, maxHeight);
  }

  private float encodeAbilities(PdfContentByte directContent,
                                IGenericCharacter character, IGenericDescription description,
                                float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          PdfAbilitiesEncoder.createWithCraftsOnly(getBaseFont(),
                                                                   getResources(),
                                                                   essenceMax),
                          1, 1, distanceFromTop, height);
  }

  private float encodeSpecialties(PdfContentByte directContent,
                                  IGenericCharacter character, IGenericDescription description,
                                  float distanceFromTop, float height)
      throws DocumentException {
    // TODO: Revise the Specialties box to gracefully add lines to fill up
    //       the allotted space, rather than taking a hard-coded parameter.
    return encodeFixedBox(directContent, character, description,
                          new PdfSpecialtiesEncoder(getResources(), getBaseFont(), 11),
                          1, 2, distanceFromTop, height);
  }

  private float encodeAttributes(PdfContentByte directContent,
                                 IGenericCharacter character, IGenericDescription description,
                                 float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          new PdfAttributesEncoder(getBaseFont(),
                                                   getResources(),
                                                   essenceMax,
                                                   getPartEncoder().isEncodeAttributeAsFavorable()),
                          1, 1, distanceFromTop, height);
  }

  private float encodeBackgrounds(PdfContentByte directContent,
                                  IGenericCharacter character, IGenericDescription description,
                                  float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          new PdfBackgroundEncoder(getResources(), getBaseFont()),
                          3, 1, distanceFromTop, height);
  }

  private float encodeVirtues(PdfContentByte directContent,
                              IGenericCharacter character, IGenericDescription description,
                              float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          new PdfVirtueEncoder(getResources(), getBaseFont()),
                          2, 1, distanceFromTop, height);
  }

  private float encodeWillpowerDots(PdfContentByte directContent,
                                    IGenericCharacter character, IGenericDescription description,
                                    float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getPartEncoder().getDotsEncoder(OtherTraitType.Willpower, 10, "Willpower"),
                          3, 1, distanceFromTop, height);
  }

  private float encodeGreatCurse(PdfContentByte directContent,
                                 IGenericCharacter character, IGenericDescription description,
                                 float distanceFromTop, float height)
      throws DocumentException {
    IPdfContentBoxEncoder encoder = getPartEncoder().getGreatCurseEncoder();
    if (encoder != null) {
      return encodeFixedBox(directContent, character, description, encoder,
                            2, 1, distanceFromTop, height);
    }
    else {
      return 0;
    }
  }

  private float encodeIntimacies(PdfContentByte directContent,
                                 IGenericCharacter character, IGenericDescription description,
                                 float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getPartEncoder().getIntimaciesEncoder(getRegistry()),
                          2, 1, distanceFromTop, height);
  }

  private float encodeLinguistics(PdfContentByte directContent,
                                  IGenericCharacter character, IGenericDescription description,
                                  float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getRegistry().getLinguisticsEncoder(),
                          3, 1, distanceFromTop, height);
  }

  private float encodeExperience(PdfContentByte directContent,
                                 IGenericCharacter character, IGenericDescription description,
                                 float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          new PdfExperienceEncoder(getResources(), getBaseFont()),
                          3, 1, distanceFromTop, height);
  }

  private boolean hasMutations(IGenericCharacter character) {
    return getRegistry().getMutationsEncoder().hasContent(character);
  }

  private boolean hasMeritsAndFlaws(IGenericCharacter character) {
    return getRegistry().getMeritsAndFlawsEncoder().hasContent(character);
  }

  private float encodeMutations(PdfContentByte directContent,
                                IGenericCharacter character, IGenericDescription description,
                                float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getRegistry().getMutationsEncoder(),
                          2, 1, distanceFromTop, height);
  }

  private float encodeMeritsAndFlaws(PdfContentByte directContent,
                                     IGenericCharacter character, IGenericDescription description,
                                     float distanceFromTop, float height)
      throws DocumentException {
    return encodeFixedBox(directContent, character, description,
                          getRegistry().getMeritsAndFlawsEncoder(),
                          2, 1, distanceFromTop, height);
  }

  private float encodeAdditional(PdfContentByte directContent,
                                 IGenericCharacter character, IGenericDescription description,
                                 float distanceFromTop, float bottom)
      throws DocumentException {
    float increment = 0;
    for (IPdfVariableContentBoxEncoder encoder : getPartEncoder().getAdditionalFirstPageEncoders()) {
      float height = encodeVariableBoxBottom(directContent, character, description, encoder,
                                             3, 1, bottom, bottom - distanceFromTop - increment);
      increment += calculateBoxIncrement(height);
    }
    return increment;
  }
}