package net.sf.anathema.character.sidereal.reporting.layout;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.template.TemplateType;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.HorizontalLineBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.PdfBoxEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.traits.FavorableTraitBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.character.reporting.pdf.rendering.page.PageConfiguration;
import net.sf.anathema.character.reporting.pdf.rendering.page.PageEncoder;
import net.sf.anathema.character.sidereal.reporting.content.SiderealCollegeContent;
import net.sf.anathema.character.sidereal.reporting.rendering.ArcaneFateInfoEncoder;
import net.sf.anathema.character.sidereal.reporting.rendering.ParadoxInfoEncoder;
import net.sf.anathema.character.sidereal.reporting.rendering.astrology.SecondEditionAstrologyInfoEncoder;
import net.sf.anathema.character.sidereal.reporting.rendering.greatcurse.ParadoxEncoder;
import net.sf.anathema.character.sidereal.reporting.rendering.resplendentdestiny.ResplendentDestinyEncoder;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identificate;

import static net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants.PADDING;

public class Sidereal2ndEditionDetailsPageEncoder implements PageEncoder {

  private final static float COLLEGE_HEIGHT = 312;
  private final static float DESTINY_HEIGHT = (COLLEGE_HEIGHT - PADDING) / 2;
  private final static float PARADOX_HEIGHT = 45;
  private final IResources resources;
  private final PdfBoxEncoder boxEncoder;
  private final PageConfiguration configuration;
  private final int fontSize;

  private static final TemplateType roninType = new TemplateType(CharacterType.SIDEREAL, new Identificate("Ronin")); //$NON-NLS-1$
  private static final TemplateType revisedRoninType = new TemplateType(CharacterType.SIDEREAL, new Identificate("RevisedRonin")); //$NON-NLS-1$
  private static final TemplateType dreamsType = new TemplateType(CharacterType.SIDEREAL, new Identificate("Dreams")); //$NON-NLS-1$
  private static final TemplateType revisedDreamsType = new TemplateType(CharacterType.SIDEREAL, new Identificate("RevisedDreams")); //$NON-NLS-1$

  public Sidereal2ndEditionDetailsPageEncoder(IResources resources, int fontSize, PageConfiguration configuration) {
    this.resources = resources;
    this.fontSize = fontSize;
    this.configuration = configuration;
    this.boxEncoder = new PdfBoxEncoder();
  }

  public void encode(Document document, SheetGraphics graphics, ReportContent content) throws DocumentException {
    if (isRonin(content.getCharacter())) {
      return;
    }
    float distanceFromTop = 0;
    float collegeHeight = 0;
    collegeHeight = encodeColleges(graphics, content, distanceFromTop);
    encodeAstrology(graphics, content, distanceFromTop);
    distanceFromTop += collegeHeight + PADDING;

    if (!isFirstAge(content.getCharacter())) {
      encodeArcaneFate(graphics, content, distanceFromTop);
    }
    encodeConnections(graphics, content, distanceFromTop);
    distanceFromTop += encodeParadoxHelp(graphics, content, distanceFromTop);
    distanceFromTop += PADDING;

    float centerDistance = distanceFromTop;
    encodeResplendentDestiny(graphics, getLeftDestinyBounds(distanceFromTop), content);
    distanceFromTop += encodeResplendentDestiny(graphics, getRightDestinyBounds(distanceFromTop), content);
    distanceFromTop += PADDING;

    centerDistance += encodeParadox(graphics, content, centerDistance);
    centerDistance += PADDING;
    encodeAcquaintances(graphics, content, centerDistance, DESTINY_HEIGHT - PARADOX_HEIGHT - PADDING);

    encodeResplendentDestiny(graphics, getLeftDestinyBounds(distanceFromTop), content);
    encodeResplendentDestiny(graphics, getCenterDestinyBounds(distanceFromTop), content);
    encodeResplendentDestiny(graphics, getRightDestinyBounds(distanceFromTop), content);
  }

  private boolean isRonin(IGenericCharacter character) {
    return character.getTemplate().getTemplateType().getSubType().getId().equals(roninType.getSubType().getId()) || character.getTemplate().getTemplateType().getSubType().getId().equals(revisedRoninType.getSubType().getId());
  }

  private boolean isFirstAge(IGenericCharacter character) {
    return character.getTemplate().getTemplateType().getSubType().getId().equals(dreamsType.getSubType().getId()) || character.getTemplate().getTemplateType().getSubType().getId().equals(revisedDreamsType.getSubType().getId());
  }

  private void encodeConnections(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = DESTINY_HEIGHT;
    Bounds boxBounds;
    if (isFirstAge(content.getCharacter())) {
      boxBounds = configuration.getFirstColumnRectangle(distanceFromTop, height, 2);
    } else {
      boxBounds = configuration.getSecondColumnRectangle(distanceFromTop, height, 1);
    }
    ContentEncoder encoder = new HorizontalLineBoxContentEncoder(4, resources, "Sidereal.Connections"); //$NON-NLS-1$
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
  }

  private float encodeAcquaintances(SheetGraphics graphics, ReportContent content, float distanceFromTop, float height) throws DocumentException {
    Bounds boxBounds = configuration.getSecondColumnRectangle(distanceFromTop, height, 1);
    ContentEncoder encoder = new HorizontalLineBoxContentEncoder(1, resources, "Sidereal.Acquaintances"); //$NON-NLS-1$
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }

  private float encodeParadox(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = PARADOX_HEIGHT;
    Bounds boxBounds = configuration.getSecondColumnRectangle(distanceFromTop, height, 1);
    ContentEncoder encoder = new ParadoxEncoder(resources);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }

  private float encodeAstrology(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = COLLEGE_HEIGHT;
    Bounds boxBounds = configuration.getSecondColumnRectangle(distanceFromTop, height, 2);
    ContentEncoder encoder = new SecondEditionAstrologyInfoEncoder(resources);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }

  private float encodeResplendentDestiny(SheetGraphics graphics, Bounds boxBounds, ReportContent content) throws DocumentException {
    ContentEncoder encoder = new ResplendentDestinyEncoder(fontSize, resources);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return boxBounds.height;
  }

  private Bounds getRightDestinyBounds(float distanceFromTop) {
    return configuration.getThirdColumnRectangle(distanceFromTop, DESTINY_HEIGHT);
  }

  private Bounds getCenterDestinyBounds(float distanceFromTop) {
    return configuration.getSecondColumnRectangle(distanceFromTop, DESTINY_HEIGHT, 1);
  }

  private Bounds getLeftDestinyBounds(float distanceFromTop) {
    return configuration.getFirstColumnRectangle(distanceFromTop, DESTINY_HEIGHT, 1);
  }

  private float encodeParadoxHelp(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = DESTINY_HEIGHT;
    Bounds boxBounds = configuration.getThirdColumnRectangle(distanceFromTop, height);
    ContentEncoder encoder = new ParadoxInfoEncoder(fontSize, resources, ExaltedEdition.SecondEdition);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }

  private float encodeArcaneFate(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = DESTINY_HEIGHT;
    Bounds boxBounds = configuration.getFirstColumnRectangle(distanceFromTop, height, 1);
    ContentEncoder encoder = new ArcaneFateInfoEncoder(fontSize, resources, ExaltedEdition.SecondEdition);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }

  private float encodeColleges(SheetGraphics graphics, ReportContent content, float distanceFromTop) throws DocumentException {
    float height = COLLEGE_HEIGHT;
    Bounds boxBounds = configuration.getFirstColumnRectangle(distanceFromTop, height, 1);
    ContentEncoder encoder = new FavorableTraitBoxContentEncoder(SiderealCollegeContent.class);
    boxEncoder.encodeBox(content, graphics, encoder, boxBounds);
    return height;
  }
}
