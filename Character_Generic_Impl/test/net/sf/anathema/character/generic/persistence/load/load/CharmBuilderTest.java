package net.sf.anathema.character.generic.persistence.load.load;

import net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants;
import net.sf.anathema.character.generic.impl.magic.persistence.CharmBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.ICharmBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.ComboRulesBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.IdStringBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.prerequisite.AttributeRequirementBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.prerequisite.CharmPrerequisiteBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.prerequisite.TraitPrerequisitesBuilder;
import net.sf.anathema.character.generic.magic.charms.CharmException;
import net.sf.anathema.character.generic.magic.charms.duration.SimpleDuration;
import net.sf.anathema.lib.exception.PersistenceException;
import org.dom4j.Element;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore("Wrong fixture - there is something odd with cost lists.")
public class CharmBuilderTest implements ICharmXMLConstants {

  // Todo: Cost, Combos
  private Element charmElement = CharmXmlTestUtils.createCharmElement("Solar.TestCharm");
  private ICharmBuilder charmBuilder = new CharmBuilder(
    new IdStringBuilder(),
    new TraitPrerequisitesBuilder(),
    new AttributeRequirementBuilder(),
    new ComboRulesBuilder(),
    new CharmPrerequisiteBuilder());

  private void removeAttribute(String attribute) {
    charmElement.remove(charmElement.attribute(attribute));
  }

  private void removeElement(String element) {
    charmElement.remove(charmElement.element(element));
  }

  @Test
  public void testNoId() throws Exception {
    try {
      removeAttribute("id"); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (PersistenceException e) {
      // nothing to do
    }
  }

  @Test
  public void testBadId() throws Exception {
    try {
      charmElement.addAttribute("id", ""); //$NON-NLS-1$ //$NON-NLS-2$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (PersistenceException e) {
      // nothing to do
    }
  }

  @Test(expected = PersistenceException.class)
  public void testNoExalt() throws Exception {
    removeAttribute("exalt"); //$NON-NLS-1$
    charmBuilder.buildCharm(charmElement);
  }

  @Test(expected = PersistenceException.class)
  public void testExaltNonExistant() throws Exception {
    charmElement.addAttribute("exalt", "NonExistant"); //$NON-NLS-1$ //$NON-NLS-2$
    charmBuilder.buildCharm(charmElement);
  }

  @Test
  public void testNoPrerequisitesAtAll() throws Exception {
    try {
      removeElement("prerequisite"); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (PersistenceException e) {
      // nothing to do
    }
  }

  @Test
  public void testEmptyIllegalPrerequisite() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteElement = prerequisitesElement.element(TAG_TRAIT);
      prerequisiteElement.remove(prerequisiteElement.attribute("id")); //$NON-NLS-1$
      prerequisiteElement.remove(prerequisiteElement.attribute("value")); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testNoIdIllegalPrerequisite() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteElement = prerequisitesElement.element(TAG_TRAIT);
      prerequisiteElement.remove(prerequisiteElement.attribute("id")); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testBadIdIllegalPrerequisite() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteElement = prerequisitesElement.element("trait"); //$NON-NLS-1$
      prerequisiteElement.addAttribute("id", "BadId"); //$NON-NLS-1$ //$NON-NLS-2$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testNoValueIllegalPrerequisite() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteElement = prerequisitesElement.element(TAG_TRAIT);
      prerequisiteElement.remove(prerequisiteElement.attribute("value")); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testBadValueIllegalPrerequisite() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteElement = prerequisitesElement.element(TAG_TRAIT);
      prerequisiteElement.addAttribute("value", "BadValue"); //$NON-NLS-1$ //$NON-NLS-2$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testNoEssence() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      prerequisitesElement.remove(prerequisitesElement.element("essence")); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testBadEssence() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      prerequisitesElement.element("essence").addAttribute("value", "BadEssence"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // Nothing to do
    }
  }

  @Test
  public void testPrerequisiteCharmNoId() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      prerequisitesElement.addElement(TAG_CHARM_REFERENCE);
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // Nothing to do
    }
  }

  @Test
  public void testPrerequisiteCharmBadId() throws Exception {
    try {
      Element prerequisitesElement = charmElement.element("prerequisite"); //$NON-NLS-1$
      Element prerequisiteCharmElement = prerequisitesElement.addElement(TAG_CHARM_REFERENCE);
      prerequisiteCharmElement.addAttribute("id", ""); //$NON-NLS-1$ //$NON-NLS-2$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // Nothing to do
    }
  }

  @Test
  public void testNoCharmTypeElement() throws Exception {
    try {
      removeElement(TAG_CHARMTYPE);
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testNoCharmTypeAttribute() throws Exception {
    try {
      Element typeElement = charmElement.element(TAG_CHARMTYPE);
      typeElement.remove(typeElement.attribute(ATTRIB_TYPE));
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testBadCharmTypeAttribute() throws Exception {
    try {
      Element typeElement = charmElement.element(TAG_CHARMTYPE);
      typeElement.addAttribute(ATTRIB_TYPE, "BadType"); //$NON-NLS-1$
      charmBuilder.buildCharm(charmElement);
      fail();
    } catch (CharmException e) {
      // nothing to do
    }
  }

  @Test
  public void testCorrectCharmWithInstantDuration() throws Exception {
    Element durationElement = charmElement.element(TAG_DURATION);
    durationElement.addAttribute(ATTRIB_DURATION, "Instant"); //$NON-NLS-1$
    assertTrue(charmBuilder.buildCharm(charmElement).getDuration() == SimpleDuration.INSTANT_DURATION);
  }

  @Test
  public void testCorrectCharmWithOtherDuration() throws Exception {
    assertFalse(charmBuilder.buildCharm(charmElement).getDuration() == SimpleDuration.PERMANENT_DURATION);
    assertFalse(charmBuilder.buildCharm(charmElement).getDuration() == SimpleDuration.INSTANT_DURATION);
  }
}