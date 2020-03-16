package io.cityos.cityosair.data.model;

import com.google.gson.annotations.SerializedName;

public class DeviceIdEntity {
  String ID;
  @SerializedName("Thing_Name")
  String thingName;

  public String getID() {
    return ID;
  }

  public String getThingName() { return thingName; }
}
