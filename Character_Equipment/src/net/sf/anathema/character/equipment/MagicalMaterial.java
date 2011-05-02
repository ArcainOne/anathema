package net.sf.anathema.character.equipment;

import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.generic.type.ICharacterTypeVisitor;
import net.sf.anathema.lib.util.IIdentificate;

public enum MagicalMaterial implements IIdentificate {
  Orichalcum, Jade, Moonsilver, Starmetal, Soulsteel, Adamant;

  public String getId() {
    return name();
  }

  public static MagicalMaterial getDefault(ICharacterType characterType) {
    final MagicalMaterial[] material = new MagicalMaterial[1];

    characterType.accept(new ICharacterTypeVisitor() {

      public void visitSolar(ICharacterType visitedType) {
        material[0] = Orichalcum;
      }

      public void visitSidereal(ICharacterType visitedType) {
        material[0] = Starmetal;
      }
      
      public void visitAlchemical(ICharacterType visitedType) {
          //material[0] = ;
        }

      public void visitMortal(ICharacterType visitedType) {
        // nothing to do
      }

      public void visitLunar(ICharacterType type) {
        material[0] = Moonsilver;
      }

      public void visitDragonKing(ICharacterType type) {
        material[0] = Orichalcum;
      }

      public void visitDB(ICharacterType visitedType) {
        material[0] = Jade;
      }

      public void visitAbyssal(ICharacterType visitedType) {
        material[0] = Soulsteel;
      }
    });
    return material[0];
  }
}