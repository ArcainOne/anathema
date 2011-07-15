package net.sf.anathema.character.view.magic;

import javax.swing.Icon;

import net.sf.anathema.character.generic.framework.magic.view.IMagicLearnProperties;

public interface IComboViewProperties extends IMagicLearnProperties {

  public Icon getFinalizeButtonIcon();
  
  public Icon getFinalizeXPButtonIcon();

  public String getFinalizeButtonToolTip();
  
  public String getFinalizeXPButtonToolTip();

  public String getAvailableComboCharmsLabel();

  public String getComboedCharmsLabel();

  public Icon getClearButtonIcon();

  public String getClearButtonToolTip();

  public Icon getCancelEditButtonIcon();

  public String getFinalizeButtonEditToolTip();

  public String getCancelButtonEditToolTip();
  
  public boolean canFinalizeWithXP();
}