package io.cityos.cityosair.data.model.map;

import io.realm.RealmObject;

public class MapMeta extends RealmObject {
  private String id;
  private String level;
  private double value;
  private String measurement;
  private String unit;
  private String deviceId;

  public MapMeta() {
  }

  public String getDeviceId() {
    return deviceId;
  }

  public MapMeta setDeviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public String getId() {
    return id;
  }

  public MapMeta setId(String id) {
    this.id = id;
    return this;
  }

  public String getLevel() {
    return level;
  }

  public MapMeta setLevel(String level) {
    this.level = level;
    return this;
  }

  public double getValue() {
    return value;
  }

  public MapMeta setValue(double value) {
    this.value = value;
    return this;
  }

  public String getMeasurement() {
    return measurement;
  }

  public MapMeta setMeasurement(String measurement) {
    this.measurement = measurement;
    return this;
  }

  public String getUnit() {
    return unit;
  }

  public MapMeta setUnit(String unit) {
    this.unit = unit;
    return this;
  }
}
