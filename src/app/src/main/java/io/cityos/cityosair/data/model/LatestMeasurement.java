package io.cityos.cityosair.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class LatestMeasurement {
  @SerializedName("device_id") String deviceId;
  private String name;
  private Boolean indoor;
  private Boolean mine;
  private Map<String, Double> latest;
  private long timestamp;

  public Map<String, Double> getLatest() {
    return latest;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getName() {
    return name;
  }

  public Boolean getIndoor() {
    return indoor;
  }

  public Boolean getMine() {
    return mine;
  }
}
