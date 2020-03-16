package io.cityos.cityosair.data.model;

import com.google.gson.annotations.SerializedName;
import io.cityos.cityosair.data.model.map.MapMeta;
import io.cityos.cityosair.data.model.map.NetworkMapMeta;
import io.cityos.cityosair.data.model.map.NewLocation;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

import java.util.Date;
import java.util.Map;

public class Device extends RealmObject {

  @PrimaryKey
  @SerializedName("device_id")
  private String id;
  private String addOn;
  private boolean active;
  private String editOn;
  private String model;
  private boolean mine;
  private Integer schemaId;
  private NewLocation location;
  private String identification;
  private String name;
  private boolean isMapDevice = false;
  @Ignore @SerializedName("map_meta")
  private Map<String, NetworkMapMeta> mapMeta;
  @SerializedName("zone_id")
  private String zoneId;
  private long timestamp;
  private boolean indoor;
  private boolean isCityDevice = false;
  private RealmList<MapMeta> mapMetas = new RealmList<>();

  public Device() {
  }

  public boolean isMapDevice() {
    return isMapDevice;
  }

  public Device setMapDevice(boolean mapDevice) {
    isMapDevice = mapDevice;
    return this;
  }

  public String getModel() {
    return model;
  }

  public boolean isActive() {
    return active;
  }

  public boolean isMine() {
    return mine;
  }

  public boolean isIndoor() {
    return indoor;
  }

  public String getDeviceId() {
    return id;
  }

  public String getIdString() {
    return id;
  }

  public void setDeviceId(String deviceId) {
    this.id = deviceId;
  }

  public String getAddOn() {
    return addOn;
  }

  public void setAddOn(String addOn) {
    this.addOn = addOn;
  }

  public String getEditOn() {
    return editOn;
  }

  public void setEditOn(String editOn) {
    this.editOn = editOn;
  }

  public Integer getSchemaId() {
    return schemaId;
  }

  public void setSchemaId(Integer schemaId) {
    this.schemaId = schemaId;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getIdentification() {
    return identification;
  }

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIndoor(Boolean indoor) {
    this.indoor = indoor;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Device setId(String id) {
    this.id = id;
    return this;
  }

  public Device setModel(String model) {
    this.model = model;
    return this;
  }

  public Device setMine(Boolean mine) {
    this.mine = mine;
    return this;
  }

  public NewLocation getLocation() {
    return location;
  }

  public Device setLocation(NewLocation location) {
    this.location = location;
    return this;
  }

  public Map<String, NetworkMapMeta> getMapMeta() {
    return mapMeta;
  }

  public Device setMapMeta(
      Map<String, NetworkMapMeta> mapMeta) {
    this.mapMeta = mapMeta;
    return this;
  }

  public RealmList<MapMeta> getMapMetas() {
    return mapMetas;
  }

  public Device setMapMetas(Map<String, NetworkMapMeta> mapMetas) {
    RealmList<MapMeta> realmMapMetas = new RealmList<>();
    for (Map.Entry<String, NetworkMapMeta> entry : mapMetas.entrySet()) {
      MapMeta mapMeta = new MapMeta();
      mapMeta.setId(entry.getKey());
      mapMeta.setLevel(entry.getValue().getLevel());
      mapMeta.setValue(entry.getValue().getValue());
      mapMeta.setUnit(entry.getValue().getUnit());
      mapMeta.setDeviceId(id);
      mapMeta.setMeasurement(entry.getValue().getMeasurement());
      realmMapMetas.add(mapMeta);
    }

    this.mapMetas = realmMapMetas;

    return this;
  }

  public AqiIndex getAQI(String sensor) {
    NetworkMapMeta mapMeta = this.mapMeta.get(sensor);
    String level = mapMeta.getLevel();
    return AqiIndex.getAQIForLevel(level);
  }

  public boolean isCityDevice() {
    return isCityDevice;
  }

  public void setCityDevice(boolean cityDevice) {
    isCityDevice = cityDevice;
  }

  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }
}
