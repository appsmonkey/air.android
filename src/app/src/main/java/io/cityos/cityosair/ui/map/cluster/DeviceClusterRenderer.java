package io.cityos.cityosair.ui.map.cluster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;
import java.util.Map;

import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapMeta;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.util.map.DevicesMapUtils;

/*
  Devices Cluster Renderer for Devices map
  Will group devices when close enough on map to reduce clutter
 */
public class DeviceClusterRenderer extends DefaultClusterRenderer<DeviceMap>
    implements ClusterManager.OnClusterClickListener<DeviceMap> {
  private static String TAG = DeviceClusterRenderer.class.getSimpleName();

  private GoogleMap googleMap;
  private LayoutInflater layoutInflater;
  private final IconGenerator clusterIconGenerator;
  private final View clusterItemView;

  public Map<String, Marker> mMarkerMap = new HashMap<>();
  public Device dashboardDevice;
  public Marker mMarker;

  public DeviceClusterRenderer(@NonNull Context context, GoogleMap map, ClusterManager<DeviceMap> clusterManager) {
    super(context, map, clusterManager);

    this.googleMap = map;

    layoutInflater = LayoutInflater.from(context);

    clusterItemView = layoutInflater.inflate(R.layout.single_cluster_marker_view, null);

    clusterIconGenerator = new IconGenerator(context);
    Drawable drawable = ContextCompat.getDrawable(context, android.R.color.transparent);
    clusterIconGenerator.setBackground(drawable);
    clusterIconGenerator.setContentView(clusterItemView);

    clusterManager.setOnClusterClickListener(this);

    googleMap.setOnCameraIdleListener(clusterManager);
    googleMap.setOnMarkerClickListener(clusterManager);
  }

  @Override
  protected void onBeforeClusterItemRendered(DeviceMap item, MarkerOptions markerOptions) {
    Context context = clusterItemView.getContext();
    // if we're going from device' dashboard to map we have dashboard device set to not null
    // this code will make boxy icon selected
    if (dashboardDevice != null && item.getDevice().getDeviceId().equals(dashboardDevice.getDeviceId())) {
      Bitmap icon = DevicesMapUtils.getSelectedBitmapIcon(context, item.getDevice());
      markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
      // set marker from marker map we need it to be able to handle click select and deselect of map's markers
      mMarker = mMarkerMap.get(item.getDevice().getDeviceId());
    } else {
      // otherwise render normal boxy icon
      Bitmap icon = DevicesMapUtils.getBitmapIcon(context, item.getDevice());
      markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
  }

  @Override
  protected void onBeforeClusterRendered(Cluster<DeviceMap> cluster, MarkerOptions markerOptions) {
    Context context = clusterItemView.getContext();

    TextView singleClusterMarkerSizeTextView = clusterItemView.findViewById(R.id.cluser_size_text_view);
    singleClusterMarkerSizeTextView.setText(String.valueOf(cluster.getSize()));
    // set background from icon for cluater
    singleClusterMarkerSizeTextView.setBackground(context.getResources().getDrawable(getIconForCluster(cluster), context.getTheme()));
    Bitmap icon = clusterIconGenerator.makeIcon();
    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
  }

  private int getIconForCluster(Cluster<DeviceMap> cluster) {
    double pm10Value = 0.0;
    double pm25Value = 0.0;
    int count = 0;

    TextView singleClusterMarkerSizeTextView = clusterItemView.findViewById(R.id.cluser_size_text_view);
    singleClusterMarkerSizeTextView.setText(String.valueOf(cluster.getSize()));

    // go through all the items, and calculate average for pm values
    for (DeviceMap deviceMap: cluster.getItems()) {
      Device device = deviceMap.getDevice();
      if (!device.isActive()) {
        continue;
      }
      for (MapMeta mapMeta : device.getMapMetas()) {
        double pm10 = 0.0;
        double pm25 = 0.0;
        if (mapMeta.getId().equals(Constants.AIR_PM10)) {
          pm10 = device.getMapMeta().get(Constants.AIR_PM10).getValue();
          if (pm10 != 0.0) {
            pm10Value += pm10;
          }
        }
        if (mapMeta.getId().equals(Constants.AIR_PM2P5)) {
          pm25 = device.getMapMeta().get(Constants.AIR_PM2P5).getValue();
          if (pm25 != 0.0) {
            pm25Value += pm25;
          }
        }
        if (mapMeta.getId().equals(Constants.AIR_PM10)) {
          if (pm10 != 0.0 || pm25 != 0.0) {
            count += 1;
          }
        }
      }
    }

    // if there were devices calculate average and return icon based on the calculated value
    if (count != 0) {
      pm10Value = pm10Value / count;
      pm25Value = pm25Value / count;

      AqiIndex aqiPm10Value = AqiIndex.getAQIForTypeWithValue(pm10Value, AQIEnum.AIR_PM10);
      AqiIndex aqiPm25Value = AqiIndex.getAQIForTypeWithValue(pm25Value, AQIEnum.AIR_PM2P5);

      AqiIndex aqi = aqiPm25Value.getLevel() > aqiPm10Value.getLevel() ? aqiPm25Value : aqiPm10Value;
      return  getClusterIcon(aqi);
    } else {
      AqiIndex aqi = AqiIndex.getAQIForLevel("");
      return getClusterIcon(aqi);
    }
  }

  private int getClusterIcon(AqiIndex aqi) {
    switch (aqi.getLevel()) {
      case 1:
        return R.drawable.box_blue_group;
      case 2:
        return R.drawable.box_green_group;
      case 3:
        return R.drawable.box_yellow_group;
      case 4:
        return R.drawable.box_orange_group;
      case 5:
        return R.drawable.box_purple_group;
      case 6:
        return R.drawable.box_red_group;
      default:
        return R.drawable.box_gray_group;
    }
  }

  @Override
  protected void onClusterItemRendered(DeviceMap clusterItem, Marker marker) {
    super.onClusterItemRendered(clusterItem, marker);

    // set tag on marker to be device of the current cluster item to be retrieved later
    marker.setTag(clusterItem.getDevice());
    // add device id and marker to hash map
    mMarkerMap.put(clusterItem.getDevice().getDeviceId(), marker);
  }

  @Override
  public boolean onClusterClick(Cluster<DeviceMap> cluster) {
    // if cluster null do nothing
    if (cluster == null) return false;
    // otherwise zoom on cluster devices
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    for (DeviceMap deviceMap : cluster.getItems())  {
      builder.include(deviceMap.getPosition());
    }
    LatLngBounds bounds = builder.build();
    try {
      googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }



  @Override
  protected boolean shouldRenderAsCluster(Cluster<DeviceMap> cluster) {
    // if size more than 1 do cluster
    return cluster.getSize() > 1;
  }
}
