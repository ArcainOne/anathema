package net.sf.anathema.framework.module.preferences;

import static net.sf.anathema.framework.presenter.action.preferences.IAnathemaPreferencesConstants.MAXIMIZE_PREFERENCE;

import net.sf.anathema.initialization.PreferenceElement;
import net.sf.anathema.lib.util.IIdentificate;

@PreferenceElement
public class MaximizePreferencesElement extends AbstractCheckBoxPreferencesElement {

  private boolean maximize = SYSTEM_PREFERENCES.getBoolean(MAXIMIZE_PREFERENCE, false);

  @Override
  protected boolean getBooleanParameter() {
    return maximize;
  }

  @Override
  protected void setValue(boolean value) {
    maximize = value;
  }

  @Override
  protected String getLabelKey() {
    return "AnathemaCore.Tools.Preferences.Maximize"; //$NON-NLS-1$
  }

  @Override
  public void savePreferences() {
    SYSTEM_PREFERENCES.putBoolean(MAXIMIZE_PREFERENCE, maximize);
  }

  @Override
  protected void resetValue() {
    maximize = SYSTEM_PREFERENCES.getBoolean(MAXIMIZE_PREFERENCE, false);
  }

  @Override
  public IIdentificate getCategory() {
    return SYSTEM_CATEGORY;
  }
}