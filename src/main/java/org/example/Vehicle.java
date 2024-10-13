package org.example;

public class Vehicle {

  private String licensePlate;
  private int timesUsed;
  private boolean competetion;
  private String type;

  public boolean isCompetetion() {
    return competetion;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Vehicle(String licensePlate, int timesUsed, boolean competetion, String type) {
    this.licensePlate = licensePlate;
    this.timesUsed = timesUsed;
    this.competetion = competetion;
    this.type = type;
  }



  public String getLicensePlate() {
    return licensePlate;
  }

  public void setLicensePlate(String licensePlate) {
    this.licensePlate = licensePlate;
  }

  public int getTimesUsed() {
    return timesUsed;
  }

  public void setTimesUsed(int timesUsed) {
    this.timesUsed = timesUsed;
  }

  public boolean getisCompetetion() {
    return competetion;
  }

  public void setCompetetion(boolean competetion) {
    this.competetion = competetion;
  }




}
