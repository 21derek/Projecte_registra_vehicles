package org.example;

public class Bicycle extends Vehicle{
  boolean electricalOrNonElectrical;

  public boolean isElectricalOrNonElectrical() {
    return electricalOrNonElectrical;
  }

  public Bicycle(String licensePlate, int timesUsed, boolean competetion, boolean isElectric,String type) {
    super(licensePlate, timesUsed, competetion,"Bicycle");
  }

  public boolean getisElectricalOrNonElectrical() {
    return electricalOrNonElectrical;
  }

  public void setElectricalOrNonElectrical(boolean electricalOrNonElectrical) {
    this.electricalOrNonElectrical = electricalOrNonElectrical;
  }
  @Override
  public String toString() {
    return "Bicycle [Matrícula: " + this.getLicensePlate() +
            ", Vegades Utilitzat: " + this.getTimesUsed() +
            ", Competició: " + (this.getisCompetetion() ? "Sí" : "No") +
            ", Elèctrica: " + (this.getisElectricalOrNonElectrical() ? "Sí" : "No") + "]";
  }
}
