  package io.cityos.cityosair.data.model.map;

import io.cityos.cityosair.data.model.Device;
import io.realm.RealmList;
import java.util.List;

public class MapData {
  private RealmList<MapZone> zones;
  private RealmList<Device> devices;

  public List<MapZone> getZones() {
    return zones;
  }

  public List<Device> getDevices() {
    return devices;
  }
}
