package net.sf.anathema.character.equipment.impl.item.model.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import net.sf.anathema.character.equipment.item.model.ICollectionFactory;
import net.sf.anathema.character.equipment.item.model.IEquipmentDatabase;
import net.sf.anathema.character.equipment.template.IEquipmentTemplate;
import net.sf.anathema.framework.itemdata.model.NonPersistableItemData;
import net.sf.anathema.lib.control.IChangeListener;
import org.jmock.example.announcer.Announcer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Db4OEquipmentDatabase extends NonPersistableItemData implements IEquipmentDatabase {

  public static final String DATABASE_FILE = "Equipment.yap"; //$NON-NLS-1$
  public static final String DATABASE_FOLDER = "equipment"; //$NON-NLS-1$
  private final ICollectionFactory collectionFactory;
  private final Announcer<IChangeListener> availableTemplatesChangeControl = Announcer.to(IChangeListener.class);
  private final ObjectContainer container;

  public Db4OEquipmentDatabase(File databaseFile) {
    this.container = EquipmentDatabaseConnectionManager.createConnection(databaseFile);
    this.collectionFactory = new Db4OCollectionFactory(container);
  }

  @Override
  public String[] getAllAvailableTemplateIds() {
    final Set<String> idSet = new HashSet<String>();
    queryContainer(new Predicate<IEquipmentTemplate>() {

      @Override
      public boolean match(IEquipmentTemplate candidate) {
        idSet.add(candidate.getName());
        return false;
      }
    });
    return idSet.toArray(new String[idSet.size()]);
  }

  private void queryContainer(Predicate<IEquipmentTemplate> predicate) {
    container.query(predicate);
  }

  @Override
  public IEquipmentTemplate loadTemplate(final String templateId) {
    ObjectSet<IEquipmentTemplate> results = container.query(new Predicate<IEquipmentTemplate>() {

      @Override
      public boolean match(IEquipmentTemplate candidate) {
        return candidate.getName().equals(templateId);
      }
    });
    if (results.isEmpty()) {
      return null;
    }
    return results.next();
  }

  @Override
  public ICollectionFactory getCollectionFactory() {
    return collectionFactory;
  }

  @Override
  public void saveTemplate(IEquipmentTemplate template) {
    container.set(template);
    container.commit();
    availableTemplatesChangeControl.announce().changeOccurred();
  }

  @Override
  public void addAvailableTemplateChangeListener(IChangeListener listener) {
    availableTemplatesChangeControl.addListener(listener);
  }

  @Override
  public void deleteTemplate(String editTemplateId) {
    delete(editTemplateId);
    availableTemplatesChangeControl.announce().changeOccurred();
  }

  private void delete(String editTemplateId) {
    IEquipmentTemplate oldTemplate = loadTemplate(editTemplateId);
    container.delete(oldTemplate);
    container.commit();
  }

  @Override
  public void updateTemplate(String editTemplateId, IEquipmentTemplate saveTemplate) {
    delete(editTemplateId);
    saveTemplate(saveTemplate);
  }
}