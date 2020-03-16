package io.cityos.cityosair.data.model.map;

import io.realm.RealmObject;

public class NetworkMapMeta extends RealmObject {
  private String key;
  private String level;
  private double value;
  private String measurement;
  private String unit;

  public String getLevel() {
    return level;
  }

  public double getValue() {
    int scale = (int) Math.pow(10, 2);
    return (double) Math.round(value * scale) / scale;
  }

  public String getMeasurement() {
    return measurement;
  }

  public String getUnit() {
    return unit;
  }
}
