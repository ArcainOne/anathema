package net.sf.anathema.character.intimacies;

import javax.swing.Icon;

import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.framework.resources.CharacterUI;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.intimacies.presenter.IntimaciesPresenter;
import net.sf.anathema.character.intimacies.view.IntimaciesView;
import net.sf.anathema.character.library.intvalue.IIconToggleButtonProperties;
import net.sf.anathema.character.library.intvalue.IntValueDisplayFactoryPrototype;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.resources.IResources;

public class IntimaciesViewFactory implements IAdditionalViewFactory {

  @Override
  public IView createView(IAdditionalModel model, final IResources resources, ICharacterType type) {
    IIconToggleButtonProperties properties = new IIconToggleButtonProperties() {
      @Override
      public Icon createStandardIcon() {
        return new CharacterUI(resources).getLinkIcon();
      }

      @Override
      public Icon createUnselectedIcon() {
        return null;
      }

      @Override
      public String getToolTipText() {
        return resources.getString("Intimacies.LockButton.Tooltip"); //$NON-NLS-1$
      }
    };
    IntimaciesView view = new IntimaciesView(
            IntValueDisplayFactoryPrototype.createWithMarkerForCharacterType(resources, type), properties);
    new IntimaciesPresenter(((IIntimaciesAdditionalModel) model).getIntimaciesModel(), model, view, resources).initPresentation();
    return view;
  }
}