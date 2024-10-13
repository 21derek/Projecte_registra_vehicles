package org.example;

public class Motorbike extends Vehicle{

  boolean has_Trunk;

  public Motorbike(String licensePlate, int timesUsed, boolean competetion, boolean has_Trunk,String type) {
    super(licensePlate, timesUsed, competetion,"Motorbike");
  }

  public boolean getishas_Trunk() {
    return has_Trunk;
  }

  public void setHas_Trunk(boolean has_Trunk) {
    this.has_Trunk = has_Trunk;
  }
  @Override
  public String toString() {
    return "Motorbike [Matrícula: " + this.getLicensePlate() +
            ", Vegades Utilitzat: " + this.getTimesUsed() +
            ", Competició: " + (this.getisCompetetion() ? "Sí" : "No") +
            ", Té maleter: " + (this.getishas_Trunk() ? "Sí" : "No") + "]";
  }
}

