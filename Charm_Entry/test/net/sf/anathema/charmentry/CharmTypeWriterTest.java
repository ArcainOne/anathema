package net.sf.anathema.charmentry;

import net.sf.anathema.character.generic.magic.charms.type.*;
import net.sf.anathema.charmentry.persistence.CharmTypeWriter;
import net.sf.anathema.dummy.character.magic.DummyCharm;
import net.sf.anathema.lib.xml.ElementUtilities;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharmTypeWriterTest {

  private CharmTypeWriter writer;
  private Element element;
  private DummyCharm charm;

  @Before
  public void setUp() throws Exception {
    writer = new CharmTypeWriter();
    element = DocumentFactory.getInstance().createElement("charm"); //$NON-NLS-1$    
    charm = new DummyCharm();
  }

  @Test
  public void testWriteType() throws Exception {
    CharmType expectedType = CharmType.ExtraAction;
    charm.setCharmType(expectedType);
    writer.write(charm, element);
    assertEquals(expectedType.getId(), element.element("charmtype").attributeValue("type")); //$NON-NLS-1$//$NON-NLS-2$
  }

  @Test
  public void testWriteAnotherType() throws Exception {
    CharmType expectedType = CharmType.Simple;
    charm.setCharmType(expectedType);
    writer.write(charm, element);
    assertEquals(expectedType.getId(), element.element("charmtype").attributeValue("type")); //$NON-NLS-1$//$NON-NLS-2$
  }

  @Test
  public void testSimpleSpecialsModel() throws Exception {
    charm.setCharmType(CharmType.Simple);
    ISimpleSpecialsModel model = new ISimpleSpecialsModel() {
      public int getSpeed() {
        return 5;
      }

      public TurnType getTurnType() {
        return TurnType.Tick;
      }

      public int getDefenseModifier() {
        return -1;
      }
    };
    charm.getCharmTypeModel().setSpecialModel(model);
    writer.write(charm, element);
    final Element charmType = element.element("charmtype"); //$NON-NLS-1$
    final Element special = charmType.element("special"); //$NON-NLS-1$
    assertEquals(5, ElementUtilities.getIntAttrib(special, "speed", 0)); //$NON-NLS-1$
    assertEquals("Tick", special.attributeValue("turntype")); //$NON-NLS-1$ //$NON-NLS-2$
    assertEquals(-1, ElementUtilities.getIntAttrib(special, "defense", 0)); //$NON-NLS-1$ 
  }

  @Test
  public void testReflexiveSpecialsModel() throws Exception {
    charm.setCharmType(CharmType.Reflexive);
    ITypeSpecialsModel model = new IReflexiveSpecialsModel() {
      public Integer getPrimaryStep() {
        return 5;
      }

      public Integer getSecondaryStep() {
        return null;
      }

      public boolean isSplitEnabled() {
        return false;
      }
    };
    charm.getCharmTypeModel().setSpecialModel(model);
    writer.write(charm, element);
    final Element charmType = element.element("charmtype"); //$NON-NLS-1$
    final Element special = charmType.element("special"); //$NON-NLS-1$
    assertEquals(5, ElementUtilities.getIntAttrib(special, "primaryStep", 0)); //$NON-NLS-1$
    assertNull(special.attributeValue("secondaryStep")); //$NON-NLS-1$
  }

  @Test
  public void testSplitReflexiveSpecialsModel() throws Exception {
    charm.setCharmType(CharmType.Reflexive);
    ITypeSpecialsModel model = new IReflexiveSpecialsModel() {
      public Integer getPrimaryStep() {
        return 3;
      }

      public Integer getSecondaryStep() {
        return 4;
      }

      public boolean isSplitEnabled() {
        return true;
      }
    };
    charm.getCharmTypeModel().setSpecialModel(model);
    writer.write(charm, element);
    final Element charmType = element.element("charmtype"); //$NON-NLS-1$
    final Element special = charmType.element("special"); //$NON-NLS-1$
    assertEquals(3, ElementUtilities.getIntAttrib(special, "primaryStep", 0)); //$NON-NLS-1$
    assertEquals(4, ElementUtilities.getIntAttrib(special, "secondaryStep", 0)); //$NON-NLS-1$
  }
}