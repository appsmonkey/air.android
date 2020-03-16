package io.cityos.cityosair.ui.map.cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.cityos.cityosair.data.model.Device;

public class DeviceMap implements ClusterItem {
  private final LatLng mPosition;
  private String mTitle;
  private String mSnippet;
  private Device mDevice;

  public DeviceMap(double lat, double lng, Device device) {
    mPosition = new LatLng(lat, lng);
    mDevice = device;

    mTitle = null;
    mSnippet = null;
  }

  DeviceMap(double lat, double lng, String title, String snippet) {
    mPosition = new LatLng(lat, lng);
    mTitle = title;
    mSnippet = snippet;
  }

  @Override
  public LatLng getPosition() {
    return mPosition;
  }

  @Override
  public String getTitle() { return mTitle; }

  @Override
  public String getSnippet() { return mSnippet; }

  public Device getDevice() { return mDevice; }
}
