package net.sf.anathema.character.generic.impl.util;

import net.sf.anathema.character.generic.util.IPointModification;

public class DefaultPointModification implements IPointModification {

  @Override
  public int getAdditionalPoints(int traitValue) {
    return 0;
  }
}