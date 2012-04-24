package net.sf.anathema.framework.module;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.framework.extension.IAnathemaExtension;
import net.sf.anathema.framework.presenter.action.preferences.IPreferencesElement;
import net.sf.anathema.initialization.Extension;
import net.sf.anathema.initialization.reflections.AnathemaReflections;
import net.sf.anathema.initialization.repository.IDataFileProvider;
import net.sf.anathema.lib.resources.IResources;

@Extension(id="net.sf.anathema.framework.module.PreferencesElementsExtensionPoint")
public class PreferencesElementsExtensionPoint implements IAnathemaExtension {

  public static final String ID = PreferencesElementsExtensionPoint.class.getName();
  private final List<IPreferencesElement> elements = new ArrayList<IPreferencesElement>();

  public void initialize(IResources resources,
		  IDataFileProvider dataFileProvider,
		  AnathemaReflections reflections) {
    // nothing to do
  }

  public IPreferencesElement[] getAllPreferencesElements() {
    return elements.toArray(new IPreferencesElement[elements.size()]);
  }

  public void addPreferencesElement(IPreferencesElement element) {
    elements.add(element);
  }
}