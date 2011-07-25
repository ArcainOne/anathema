package net.sf.anathema.character.generic.magic.charms.special;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.util.IIdentificate;

public interface ISubeffect extends IIdentificate {
	
  public ICharm getCharm();

  public boolean isLearned();

  public boolean isCreationLearned();

  public void addChangeListener(IChangeListener listener);

  public void setLearned(boolean learned);

  public void setCreationLearned(boolean creationLearned);

  public void setExperienceLearned(boolean experienceLearned);
}