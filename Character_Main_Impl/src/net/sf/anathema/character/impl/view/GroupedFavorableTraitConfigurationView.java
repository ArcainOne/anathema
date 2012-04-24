package net.sf.anathema.character.impl.view;

import javax.swing.JComponent;

import net.sf.anathema.character.library.intvalue.IIconToggleButtonProperties;
import net.sf.anathema.framework.value.IIntValueDisplayFactory;
import net.sf.anathema.character.library.intvalue.IToggleButtonTraitView;
import net.sf.anathema.character.library.trait.IModifiableCapTrait;
import net.sf.anathema.character.library.trait.view.GroupedTraitView;
import net.sf.anathema.character.library.trait.view.SimpleTraitView;
import net.sf.anathema.character.view.IGroupedFavorableTraitConfigurationView;

public class GroupedFavorableTraitConfigurationView implements IGroupedFavorableTraitConfigurationView {

  private final GroupedTraitView groupedTraitView;
  private final IIntValueDisplayFactory markerIntValueDisplayFactory;
  private final IIntValueDisplayFactory markerLessIntValueDisplayFactory;
  private final JComponent parent;

  public GroupedFavorableTraitConfigurationView(
      JComponent parent,
      int columnCount,
      IIntValueDisplayFactory factoryWithMarker,
      IIntValueDisplayFactory factoryWithoutMarker) {
    this.parent = parent;
    this.groupedTraitView = new GroupedTraitView(parent, columnCount);
    this.markerIntValueDisplayFactory = factoryWithMarker;
    this.markerLessIntValueDisplayFactory = factoryWithoutMarker;
  }

  @Override
  public JComponent getComponent() {
    return parent;
  }

  @Override
  public IToggleButtonTraitView<SimpleTraitView> addTraitView(
      String labelText,
      int value,
      int maxValue,
      IModifiableCapTrait trait,
      boolean selected,
      IIconToggleButtonProperties properties) {
    return groupedTraitView.addTraitView(labelText, value, maxValue, trait, selected, properties, markerIntValueDisplayFactory);
  }

  @Override
  public IToggleButtonTraitView< ? > addMarkerLessTraitView(
      String labelText,
      int value,
      int maxValue,
      IModifiableCapTrait trait,
      boolean selected,
      IIconToggleButtonProperties properties) {
    return groupedTraitView.addTraitView(
        labelText,
        value,
        maxValue,
        trait,
        selected,
        properties,
        markerLessIntValueDisplayFactory);
  }

  @Override
  public void startNewTraitGroup(String groupLabel) {
    groupedTraitView.startNewGroup(groupLabel);
  }
}