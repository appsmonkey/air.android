package io.cityos.cityosair.ui.map;

import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapZone;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapDataSingleton {

  Map<String, MapZone> mapZones;
  List<Device> deviceList;

  @Inject
  public MapDataSingleton() {
  }

  public Map<String, MapZone> getMapZones() {
    return mapZones;
  }

  public MapDataSingleton setMapZones(
      Map<String, MapZone> mapZones) {
    this.mapZones = mapZones;
    return this;
  }

  public List<Device> getDeviceList() {
    return deviceList;
  }

  public MapDataSingleton setDeviceList(
      List<Device> deviceList) {
    this.deviceList = deviceList;
    return this;
  }

  public void logout() {
    if (deviceList != null && deviceList.size() > 0) {
      for (Device device : deviceList) {
        device.setMine(false);
      }
    }
  }
}
