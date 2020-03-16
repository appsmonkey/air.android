package io.cityos.cityosair.ui.map;

import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapZone;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;

public interface MapEventHandler {
  void infoWindowClicked(Device device);
  void infoWindowClicked(DeviceMeasurementCollection deviceMeasurementCollection);
}
