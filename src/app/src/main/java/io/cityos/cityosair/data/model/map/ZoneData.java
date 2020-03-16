package io.cityos.cityosair.data.model.map;

import com.google.gson.annotations.SerializedName;

import io.cityos.cityosair.data.model.AqiIndex;
import io.realm.RealmObject;

public class ZoneData extends RealmObject {

  @SerializedName("sensor_id") private String sensorId;
  private String name;
  private String level;
  private double value;
  private String measurement;
  private String unit;

  public ZoneData() {
  }

  public String getName() {
    return name;
  }

  public String getLevel() {
    return level;
  }

  public double getValue() {
    int scale = (int) Math.pow(10, 1);
    return (double) Math.round(value * scale) / scale;
  }

  public String getMeasurement() {
    return measurement;
  }

  public String getUnit() {
    return unit;
  }

  public AqiIndex getAQI() {
    return AqiIndex.getAQIForLevel(level);
  }

  public String getSensorId() {
    return sensorId;
  }

  public void setSensorId(String sensorId) {
    this.sensorId = sensorId;
  }
}
