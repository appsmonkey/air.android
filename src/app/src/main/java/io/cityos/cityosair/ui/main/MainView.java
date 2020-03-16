package io.cityos.cityosair.ui.main;

import java.util.List;

import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.ui.map.BaseView;

public interface MainView extends BaseView {
  void setContentForDevice(DeviceMeasurementCollection deviceMeasurementCollection, Device device);
  void setContentForMeasurement(DeviceMeasurementCollection deviceMeasurementCollection);
  void setCityAir();
  void devicesRefreshed(List<Device> devices);
  void setContentLoading();
  void removeContentLoading();
}
