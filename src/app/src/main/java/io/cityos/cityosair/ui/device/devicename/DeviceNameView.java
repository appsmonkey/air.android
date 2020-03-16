package io.cityos.cityosair.ui.device.devicename;

import io.cityos.cityosair.ui.map.BaseView;

public interface DeviceNameView extends BaseView {
  void deviceAdded(String token);
  void addDeviceError();
}
