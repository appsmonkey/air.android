package io.cityos.cityosair.ui.map;

import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapPlace;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import java.util.List;

public interface MapView extends BaseView {
  void devicesLoaded(List<Device> devices);
  void cachedDevicesLoaded(List<Device> devices);
  void placesLoaded(List<MapPlace> mapPlaces);
  void loadPlaces();
}
