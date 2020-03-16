package io.cityos.cityosair.util.map;

import java.util.Comparator;

import io.cityos.cityosair.data.model.Device;

public class DevicesComparator implements Comparator<Device> {
  @Override
  public int compare(Device device1, Device device2) {
    return device1.getName().compareTo(device2.getName());
  }
}