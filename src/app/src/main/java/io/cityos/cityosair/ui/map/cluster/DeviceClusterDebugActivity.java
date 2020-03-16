package io.cityos.cityosair.ui.map.cluster;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.cityos.cityosair.R;

/*
  Activity used to debug cluster issues
  Leaving for any future problems
 */
public class DeviceClusterDebugActivity extends BaseClusterActivity {

  private static final double DEFAULT_RADIUS = 0.00003;
  private static final String DEFAULT_DELETE_LIST = "itemsDeleted";
  private static final String DEFAULT_ADDED_LIST = "itemsAdded";

  private DeviceClusterManager<DeviceMap> mClusterManager;
  private Map<String, List<DeviceMap>> mItemsCache;

  public DeviceClusterDebugActivity() {
    mItemsCache = new HashMap<>();
    mItemsCache.put(DEFAULT_ADDED_LIST, new ArrayList<>());
    mItemsCache.put(DEFAULT_DELETE_LIST, new ArrayList<>());
  }

  protected void startTest() {
    getMap().moveCamera(CameraUpdateFactory
        .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(43.852329, 18.370026), 12)));

    mClusterManager = new DeviceClusterManager<>(this, getMap());

    getMap().setOnMarkerClickListener(mClusterManager);
    getMap().setOnCameraMoveListener(() -> {

      // get markers back to the original position if they were relocated
      if (getMap().getCameraPosition().zoom < getMap().getMaxZoomLevel()) {
        mClusterManager.removeItems(mItemsCache.get(DEFAULT_ADDED_LIST));
        mClusterManager.addItems(mItemsCache.get(DEFAULT_DELETE_LIST));
        mClusterManager.cluster();

        mItemsCache.get(DEFAULT_ADDED_LIST).clear();
        mItemsCache.get(DEFAULT_DELETE_LIST).clear();
      }
    });

    mClusterManager.setOnClusterClickListener(cluster -> {
      float maxZoomLevel = getMap().getMaxZoomLevel();
      float currentZoomLevel = getMap().getCameraPosition().zoom;

      // only show markers if users is in the max zoom level
      if (currentZoomLevel != maxZoomLevel) {
        return false;
      }

      if (!mClusterManager.itemsInSameLocation(cluster)) {
        return false;
      }

      // relocate the markers around the current markers position
      int counter = 0;
      float rotateFactor = (360 / cluster.getItems().size());
      for (DeviceMap item : cluster.getItems()) {
        double lat = item.getPosition().latitude + (DEFAULT_RADIUS * Math.cos(++counter * rotateFactor));
        double lng = item.getPosition().longitude + (DEFAULT_RADIUS * Math.sin(counter * rotateFactor));
        DeviceMap copy = new DeviceMap(lat, lng, item.getTitle(), item.getSnippet());

        mClusterManager.removeItem(item);
        mClusterManager.addItem(copy);
        mClusterManager.cluster();

        mItemsCache.get(DEFAULT_ADDED_LIST).add(copy);
        mItemsCache.get(DEFAULT_DELETE_LIST).add(item);
      }

      return true;
    });

    try {
      readItems();

      mClusterManager.cluster();
    } catch (JSONException e) {
      Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
    }
  }

  private void readItems() throws JSONException {
    InputStream inputStream = getResources().openRawResource(R.raw.markers_same_location);
    List<DeviceMap> items = new DeviceMapReader().read(inputStream);

    mClusterManager.addItems(items);
  }

  private class DeviceClusterManager<T extends ClusterItem> extends ClusterManager<T> {

    DeviceClusterManager(Context context, GoogleMap map) {
      super(context, map);
    }

    boolean itemsInSameLocation(Cluster<T> cluster) {
      LinkedList<T> items = new LinkedList<>(cluster.getItems());
      T item = items.remove(0);

      double longitude = item.getPosition().longitude;
      double latitude = item.getPosition().latitude;

      for (T t : items) {
        if (Double.compare(longitude, t.getPosition().longitude) != 0 && Double.compare(latitude, t.getPosition().latitude) != 0) {
          return false;
        }
      }

      return true;
    }

    void removeItems(List<T> items) {

      for (T item : items) {
        removeItem(item);
      }
    }
  }
}