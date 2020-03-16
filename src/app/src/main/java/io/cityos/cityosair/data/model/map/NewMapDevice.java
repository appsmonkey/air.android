package io.cityos.cityosair.data.model.map;

import com.google.gson.annotations.SerializedName;
import io.cityos.cityosair.data.model.AqiIndex;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import java.util.Map;

public class NewMapDevice extends RealmObject {
  @SerializedName("device_id") private String deviceId;
  private String name;
  private Boolean active;
  private String model;
  private Boolean mine;
  private NewLocation location;
  private boolean indoor;
  @Ignore @SerializedName("map_meta")
  private Map<String, NetworkMapMeta> mapMeta;
  private RealmList<MapMeta> mapMetas = new RealmList<>();

  public NewMapDevice() {
  }

  public boolean isIndoor() {
    return indoor;
  }

  public NewMapDevice setIndoor(boolean indoor) {
    this.indoor = indoor;
    return this;
  }

  public RealmList<MapMeta> getMapMetas() {
    return mapMetas;
  }

  public Map<String, NetworkMapMeta> getMapMeta() {
    return mapMeta;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getName() {
    return name;
  }

  public boolean isActive() {
    return active;
  }

  public String getModel() {
    return model;
  }

  public boolean isMine() {
    return mine;
  }

  public NewLocation getLocation() {
    return location;
  }

  public AqiIndex getAQI(String sensor) {
    return AqiIndex.getAQIForLevel(mapMeta.get(sensor).getLevel());
  }

  public NewMapDevice setMapMetas(Map<String, NetworkMapMeta> mapMetas) {
    RealmList<MapMeta> realmMapMetas = new RealmList<>();
    for (Map.Entry<String, NetworkMapMeta> entry : mapMetas.entrySet()) {
      MapMeta mapMeta = new MapMeta();
      mapMeta.setId(entry.getKey());
      mapMeta.setLevel(entry.getValue().getLevel());
      mapMeta.setValue(entry.getValue().getValue());
      mapMeta.setUnit(entry.getValue().getUnit());
      mapMeta.setMeasurement(entry.getValue().getMeasurement());
      realmMapMetas.add(mapMeta);
    }

    this.mapMetas = realmMapMetas;

    return this;
  }
}