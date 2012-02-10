package net.sf.anathema.character.generic.template.magic;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.spells.CircleType;

public interface ISpellMagicTemplate {

  public CircleType[] getSorceryCircles();

  public CircleType[] getNecromancyCircles();

  public boolean canLearnSorcery();
  
  public CircleType[] getProtocolCircles();

  public boolean canLearnNecromancy();
  
  public boolean canLearnSpellMagic();

  public boolean knowsSorcery(ICharm[] knownCharms);

  public boolean knowsNecromancy(ICharm[] knownCharms);
  
  public boolean knowsProtocols();
  
  public boolean knowsSpellMagic();
  
  public boolean knowsSpellMagic(ICharm[] knownCharms, CircleType circle);
  
  public boolean canLearnSpell(ISpell spell, ICharm[] knownCharms);
}