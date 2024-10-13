package org.example;

public class Car extends Vehicle{

  int numberDoors;

  public Car(String licensePlate, int timesUsed, boolean competetion, int numberDoors, String type) {
    super(licensePlate, timesUsed, competetion,"car");
    this.numberDoors = numberDoors;;
  }

  public int getNumberDoors() {
    return numberDoors;
  }

  public void setNumberDoors(int numberDoors) {
    this.numberDoors = numberDoors;
  }
  @Override
  public String toString() {
    return "Car [Matrícula: " + this.getLicensePlate() +
            ", Vegades Utilitzat: " + this.getTimesUsed() +
            ", Competició: " + (this.getisCompetetion() ? "Sí" : "No") +
            ", Portes: " + this.getNumberDoors() + "]";
  }
}
