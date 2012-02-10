package net.sf.anathema.character.impl.persistence;

import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.ATTRIB_AGE;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.ATTRIB_TYPE;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.TAG_CASTE;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.TAG_CHARACTER_CONCEPT;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.TAG_CONCEPT;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.TAG_MOTIVATION;
import static net.sf.anathema.character.impl.persistence.ICharacterXmlConstants.TAG_NATURE;
import net.sf.anathema.character.generic.caste.ICasteCollection;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.model.ICharacterDescription;
import net.sf.anathema.character.model.IIntegerDescription;
import net.sf.anathema.character.model.ITypedDescription;
import net.sf.anathema.character.model.concept.ICharacterConcept;
import net.sf.anathema.character.model.concept.IMotivation;
import net.sf.anathema.character.model.concept.INature;
import net.sf.anathema.character.model.concept.INatureType;
import net.sf.anathema.character.model.concept.IWillpowerRegainingConceptVisitor;
import net.sf.anathema.character.model.concept.NatureProvider;
import net.sf.anathema.framework.persistence.TextPersister;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.xml.ElementUtilities;

import org.dom4j.Element;

public class CharacterConceptPersister {

  private final TextPersister textPersister = new TextPersister();

  public void save(Element parent, ICharacterConcept characterConcept) {
    final Element characterConceptElement = parent.addElement(TAG_CHARACTER_CONCEPT);
    saveCaste(characterConceptElement, characterConcept.getCaste());
    saveAge(characterConceptElement, characterConcept.getAge());
    characterConcept.getWillpowerRegainingConcept().accept(new IWillpowerRegainingConceptVisitor() {
      public void accept(INature nature) {
        saveNature(characterConceptElement, nature);
      }

      public void accept(IMotivation motivation) {
        textPersister.saveTextualDescription(characterConceptElement, TAG_MOTIVATION, motivation.getDescription());
      }
    });
  }

  private void saveCaste(Element parent, ITypedDescription<ICasteType> caste) {
    ICasteType casteType = caste.getType();
    if (casteType.getId() != null) {
      Element casteElement = parent.addElement(TAG_CASTE);
      casteElement.addAttribute(ATTRIB_TYPE, casteType.getId());
    }
  }
  
  private void saveAge(Element parent, IIntegerDescription age) {
    parent.addAttribute(ATTRIB_AGE, Integer.toString(age.getValue()));
  }

  private void saveNature(Element parent, INature nature) {
    Element natureElement = parent.addElement(TAG_NATURE);
    INatureType natureType = nature.getDescription().getType();
    if (natureType != null) {
      natureElement.addAttribute(ATTRIB_TYPE, natureType.getId());
    }
  }

  public void load(
      Element parent,
      ICharacterConcept characterConcept,
      ICharacterDescription description,
      ICasteCollection casteCollection) throws PersistenceException {
    final Element conceptElement = parent.element(TAG_CHARACTER_CONCEPT);
    loadCaste(conceptElement, characterConcept, casteCollection);
    loadAge(conceptElement, characterConcept);
    characterConcept.getWillpowerRegainingConcept().accept(new IWillpowerRegainingConceptVisitor() {
      public void accept(INature nature) {
        loadNature(conceptElement, nature);
      }

      public void accept(IMotivation motivation) {
        textPersister.restoreTextualDescription(conceptElement, TAG_MOTIVATION, motivation.getDescription());
      }
    });
    textPersister.restoreTextualDescription(conceptElement, TAG_CONCEPT, description.getConcept());
  }

  private void loadCaste(Element parent, ICharacterConcept characterConcept, ICasteCollection casteCollection)
      throws PersistenceException {
    Element casteElement = parent.element(TAG_CASTE);
    if (casteElement == null) {
      return;
    }
    String casteTypeId = ElementUtilities.getRequiredAttrib(casteElement, ATTRIB_TYPE);
    characterConcept.getCaste().setType(casteCollection.getById(casteTypeId));
  }
  
  private void loadAge(Element parent, ICharacterConcept characterConcept)
      throws PersistenceException {
    int age = ElementUtilities.getIntAttrib(parent, ATTRIB_AGE, 0);
    characterConcept.getAge().setValue(age);
  }

  private void loadNature(Element parent, INature nature) {
    Element natureElement = parent.element(TAG_NATURE);
    if (natureElement == null) {
      return;
    }
    String natureTypeId = natureElement.attributeValue(ATTRIB_TYPE);
    if (natureTypeId != null) {
      nature.getDescription().setType(NatureProvider.getInstance().getById(natureTypeId));
    }
  }
}