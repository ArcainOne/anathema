package net.sf.anathema.charmentry.model.data;

public class ConfigurableCostList implements IConfigurableCostList {

  private final IConfigurableCost essenceCost = new ConfigurableCost();
  private final IConfigurableCost willpowerCost = new ConfigurableCost();
  private final IConfigurableHealthCost healthCost = new ConfigurableHealthCost();
  private final IConfigurableCost xpCost = new ConfigurableCost();

  @Override
  public IConfigurableCost getEssenceCost() {
    return essenceCost;
  }

  @Override
  public IConfigurableCost getWillpowerCost() {
    return willpowerCost;
  }

  @Override
  public IConfigurableHealthCost getHealthCost() {
    return healthCost;
  }

  @Override
  public IConfigurableCost getXPCost() {
    return xpCost;
  }
}