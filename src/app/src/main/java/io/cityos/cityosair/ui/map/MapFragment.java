package io.cityos.cityosair.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.cityos.cityosair.BuildConfig;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapPlace;
import io.cityos.cityosair.data.model.map.MapZone;
import io.cityos.cityosair.data.model.map.ZoneData;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.ui.base.fragment.BasePagerFragment;
import io.cityos.cityosair.ui.main.MainTabFragment;
import io.cityos.cityosair.ui.map.cluster.DeviceClusterRenderer;
import io.cityos.cityosair.ui.map.cluster.DeviceMap;
import io.cityos.cityosair.util.map.DevicesMapUtils;
import io.cityos.cityosair.util.map.MapSegmentControl;
import io.reactivex.functions.Function;
import io.realm.RealmList;

public class MapFragment extends BasePagerFragment
    implements OnMapReadyCallback, MapView, MapEventHandler, ClusterManager.OnClusterItemClickListener<DeviceMap> {

  private String TAG = MapFragment.class.getSimpleName();

  @Inject Context mContext;
  @Inject MapPresenter mMapPresenter;
  @Inject MapDataSingleton mMapDataSingleton;

  @BindView(R.id.map_segment)
  public MapSegmentControl mMapSegmentControl;

  private int mDefaultFillOpacity = 102;
  private int mTappedFillOpacity = 153;

  private InfoMapFragment mInfoFragment;
  private boolean mIsShown = false;
  private Animation mSlideUpAnimation, mSlideDownAnimation;
  private GoogleMap mMap;
  private HashMap<String, MapZone> mPolyReadingMap = new HashMap<>();
  private List<Polygon> mPolygons = new ArrayList<>();
  private Polygon mCurrentPolygon;
  private Device mLastSelectedDevice;
  private Marker mLastSelectedMarker;
  public MapPresenter.MAP_FILTER mDevicesFilter = MapPresenter.MAP_FILTER.OUTDOOR;
  private List<Device> mFilteredDevices = new ArrayList<>();
  private ClusterManager<DeviceMap> mClusterManager;
  private DeviceClusterRenderer mClusterRenderer;
  private Boolean isFromDashboard = false;
  private DeviceMeasurementCollection mDeviceMeasurementCollection;
  private List<MapPlace> mMapPlaces;

  @OnClick(R.id.btn_sensor)
  void onSensorBtnClicked() {
    Log.d(TAG, "onSensorBtnClicked()");
    mClusterManager.clearItems();
    ((MainTabFragment) getParentFragment()).navigateToCityInfo();
  }

  @OnClick(R.id.btn_menu)
  void menuClicked() {
    Log.d(TAG, "menuClicked()");
    hideInfoWindow();
    ((MainTabFragment) getParentFragment()).openMenuClicked("", true);
  }

  private void setPolygon(Polygon polygon) {
    if (mCurrentPolygon != null) {
      mCurrentPolygon.setFillColor(ColorUtils.setAlphaComponent(mCurrentPolygon.getFillColor(), mDefaultFillOpacity));
    }

    mCurrentPolygon = polygon;
  }

  public static Function<Void, MapFragment> getFragmentCreator() {
    return aVoid -> new MapFragment();
  }

  public void centerMapOnCityAndSetDefaultZoom() {
    if (mMap != null) {
      mMap.animateCamera(CameraUpdateFactory
          .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(43.852329, 18.370026), 12)));
    }
  }

  public void centerMapOnCityAndCreateMarkers() {
    if (mMap != null) {
      mMap.animateCamera(CameraUpdateFactory
          .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(43.852329, 18.370026), 12)));
    }
    mMapSegmentControl.toggleUI(0);
    clearMap();
    // get map devices for currently selected segment
    mMapPresenter.getMapDevices(mDevicesFilter);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate()");
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onViewCreated()");
    super.onViewCreated(view, savedInstanceState);
    mMapPresenter.setView(this);
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return mMapPresenter;
  }

  @Override
  public void onFragmentShown() {
    Log.d(TAG, "onFragmentShown()");
    // this method is called every time this fragment gets focus
    // if it was already loaded and selected device is null center on city with default zoom
    if (mLastSelectedDevice == null && mMap != null) {
      centerMapOnCityAndSetDefaultZoom();
    }
    // set market null to avoid marker resource leak and crash when setting icon on markers when marker's icon set on out of screen marker
    mLastSelectedMarker = null;

    super.onFragmentShown();
  }

  @Override
  public void onStart() {
    Log.d(TAG, "onStart()");
    super.onStart();

    SupportMapFragment mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.devices_map);
    mapView.getMapAsync(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView()");
    View view = inflater.inflate(R.layout.fragment_map, container, false);

    ButterKnife.bind(this, view);

    mSlideUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_animation);
    mSlideDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_animation);

    mInfoFragment = (InfoMapFragment) getChildFragmentManager().findFragmentById(R.id.info_fragment);
    mInfoFragment.setClickListener(this);
    mInfoFragment.getView().setVisibility(View.GONE);

    mMapSegmentControl.bringToFront();

    boolean isDeviceIndoor = false;
    mMapSegmentControl.toggleUI(isDeviceIndoor ? 1 : 0);

    // setup segmented control event listener to filter devices
    mMapSegmentControl.setMapSegmentEventListener(index -> {
      hideInfoWindow();
      switch (index) {
        case 0:
          mDevicesFilter = MapPresenter.MAP_FILTER.OUTDOOR;
          break;
        case 1:
          mDevicesFilter = MapPresenter.MAP_FILTER.INDOOR;
          break;
        default:
          mDevicesFilter = MapPresenter.MAP_FILTER.MINE;
          break;
      }
      // selected marker is now null since map changed - avoid crash
      mLastSelectedMarker = null;
      // get devices and data based on filter
      mMapPresenter.getMapDevices(mDevicesFilter);
    });

    return view;
  }

  @Override public void onResume() {
    Log.d(TAG, "onResume()");
    super.onResume();
  }

  @Override public void onDestroyView() {
    Log.d(TAG, "onDestroyView()");
    super.onDestroyView();
  }

  @Override public void onPause() {
    Log.d(TAG, "onPause()");
    super.onPause();
  }

  @Override public void onStop() {
    Log.d(TAG, "onStop()");
    super.onStop();
  }

  public void clearMap() {
    mClusterManager.clearItems();
    mClusterManager.cluster();
    for (Polygon polygon : mPolygons) {
      polygon.remove();
    }
    mPolygons.clear();
  }

  public void showDeviceDetails(DeviceMeasurementCollection deviceMeasurementCollection, boolean isIndoor) {
    Log.d(TAG, "showDeviceDetails()");
    // if there's no measurement data this is first entry from dashboard. set flat entering from dashboard true
    if (!deviceMeasurementCollection.getDeviceId().equals("")) {
      isFromDashboard = true;
    }
    mMapSegmentControl.toggleUI(isIndoor ? 1 : 0);
    mDevicesFilter = isIndoor ? MapPresenter.MAP_FILTER.INDOOR : MapPresenter.MAP_FILTER.OUTDOOR;
    mDeviceMeasurementCollection = deviceMeasurementCollection;
    mMapPresenter.getMapDevices(mDevicesFilter);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    Log.d(TAG, "onMapReady: " + googleMap);

    if (mMap == null) {
      mMap = googleMap;

      centerMapOnCityAndSetDefaultZoom();

      mMap.setOnMapClickListener(latLng -> hideInfoWindow());

      // setup on polygon click
      mMap.setOnPolygonClickListener(polygon -> {
        if (polygon == null) {
          return;
        }

        polygon.setFillColor(ColorUtils.setAlphaComponent(polygon.getFillColor(), mTappedFillOpacity));

        setPolygon(polygon);

        // calculate zone values
        MapZone mapZone = mPolyReadingMap.get(polygon.getId());
        if (mapZone != null && mapZone.getZoneData() != null && mapZone.getZoneData().size() > 0) {
          ZoneData pm10Data = mapZone.getZoneData().first();
          ZoneData pm25Data = mapZone.getZoneData().last();
          for (ZoneData zoneData : mapZone.getZoneData()) {
            if (zoneData.getSensorId().equals(Constants.AIR_PM2P5)) {
              pm25Data = zoneData;
            } else if (zoneData.getSensorId().equals(Constants.AIR_PM10)) {
              pm10Data = zoneData;
            }
          }

          // show info for zone data
          showInfoWindow(mapZone, pm25Data.getAQI(), pm25Data.getValue(), pm10Data.getValue(), null);
        } else {
          hideInfoWindow();
        }
      });

      // on map move reset and set markers
      mMap.setOnCameraMoveStartedListener(reason -> {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
          hideInfoWindow();
        }

        // if there was selected marker and device deselect it
        if (mLastSelectedMarker != null && mLastSelectedDevice != null) {
          Bitmap defaultIcon = DevicesMapUtils.getBitmapIcon(getContext(), mLastSelectedDevice);
          try {
            mLastSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultIcon));
          } catch (Exception ex) {
            if (!BuildConfig.DEBUG) {
              Crashlytics.log(Log.ERROR, TAG, ex.toString());
            }
            Log.e(TAG, ex.toString());
          }
        }
        // otherwise if cluster's marker holder is not null, get cached devices for speed
        // and set renderer marker to null
        else if (mClusterRenderer.mMarker != null) {
          mClusterManager.clearItems();
          mMapPresenter.getCachedMapDevices(mDevicesFilter);
          mClusterRenderer.mMarker = null;
        }

        if (mCurrentPolygon != null) {
          setPolygon(null);
        }

        hideInfoWindow();
      });

      // setup cluster manager
      mClusterManager = new ClusterManager<>(getContext(), mMap);
      mClusterRenderer = new DeviceClusterRenderer(getContext(), mMap, mClusterManager);
      mClusterManager.setRenderer(mClusterRenderer);
      mClusterManager.setOnClusterItemClickListener(this);

      // get map devices and data
      mMapPresenter.getMapDevices(mDevicesFilter);
    }
  }

  public void reloadMap() {
    Log.d(TAG, "reloadMap()");
    hideInfoWindow();
    mMap.clear();
    mLastSelectedMarker = null;
    mMapPresenter.getMapDevices(mDevicesFilter);
  }

  @Override
  public void devicesLoaded(List<Device> devices) {
    Log.d(TAG, "devicesLoaded()");
    // if flow from dashboard get device from devices' list, set renderer's device as the one
    // for the dashboard's device and set last selected device to be the dashboard's device
    // since this is first time entry on map from dashboard with the device show info window on zoom on device
    if (isFromDashboard) {
      isFromDashboard = false;
      Device device = getDeviceFromList(devices, mDeviceMeasurementCollection.getDeviceId());
      mClusterRenderer.dashboardDevice = device;
      mLastSelectedDevice = device;
      setDashboardDevice(device);
    } else {
      // reset renderer's dashboard device to null so it won't be rendered as selected
      mClusterRenderer.dashboardDevice = null;
    }
    // filter devices and create markers
    mFilteredDevices = getFilteredDevices(devices);
    createMarkers(mFilteredDevices);
  }

  @Override
  public void cachedDevicesLoaded(List<Device> devices) {
    // if flow from dashboard get device from devices' list, set renderer's device as the one
    // for the dashboard's device and set last selected device to be the dashboard's device
    // since this is first time entry on map from dashboard with the device show info window on zoom on device
    if (isFromDashboard) {
      isFromDashboard = false;
      Device device = getDeviceFromList(devices, mDeviceMeasurementCollection.getDeviceId());
      mClusterRenderer.dashboardDevice = device;
      mLastSelectedDevice = device;
      setDashboardDevice(device);
    } else {
      // reset renderer's dashboard device to null so it won't be rendered as selected
      mClusterRenderer.dashboardDevice = null;
    }
    // get devices for filter
    mMapPresenter.getMapDevices(mDevicesFilter);
  }

  private List<Device> getFilteredDevices(List<Device> devices) {
    List<Device> filtered = new ArrayList<>();
    for (Device device : devices) {
      if (mDevicesFilter == MapPresenter.MAP_FILTER.INDOOR) {
        if (device.isIndoor()) {
          filtered.add(device);
        }
      } else if (mDevicesFilter == MapPresenter.MAP_FILTER.OUTDOOR) {
        if (!device.isIndoor()) {
          filtered.add(device);
        }
      } else if (mDevicesFilter == MapPresenter.MAP_FILTER.MINE) {
        if (device.isMine()) {
          filtered.add(device);
        }
      }
    }
    return filtered;
  }

  private void setDashboardDevice(Device device) {
    //zoom on device and get data for device's info window and show info window
    mClusterRenderer.mMarker = null;
    zoomOnDevice(device);
    String sensor = DevicesMapUtils.getSensor(device);
    Double temperature = null;
    if (!device.getMapMeta().get(Constants.AIR_TEMPERATURE).getLevel().equals("")) {
      temperature = device.getMapMeta().get(Constants.AIR_TEMPERATURE).getValue();
    }
    showInfoWindow(device, device.getAQI(sensor),
        device.getMapMeta().get(Constants.AIR_PM2P5).getValue(),
        device.getMapMeta().get(Constants.AIR_PM10).getValue(),
        temperature);
  }

  private void zoomOnDevice(Device device) {
    if (device != null) {
      CameraUpdate location = CameraUpdateFactory.newCameraPosition(
          CameraPosition.fromLatLngZoom(
              new LatLng(device.getLocation().getLat(), device.getLocation().getLng()), 17));
      mMap.animateCamera(location);
    } else {
      centerMapOnCityAndSetDefaultZoom();
    }
  }

  private Device getDeviceFromList(List<Device> devices, String deviceId) {
    for (Device device : devices) {
      if (device.getDeviceId().equals(deviceId)) {
        return device;
      }
    }
    return null;
  }

  @Override
  public void placesLoaded(List<MapPlace> mapPlaces) {
    Log.d(TAG, "placesLoaded()");
    // draw polygons for zones
    mMapPlaces = mapPlaces;
    drawPolygons(mapPlaces);
  }

  @Override
  public void loadPlaces() {
    Log.d(TAG, "loadPlaces()");
    // get zones
    mMapPresenter.getMapPlaces(getResources());
  }

  private void drawPolygons(List<MapPlace> places) {
    Log.d(TAG, "drawPolygons()");
    for (Polygon polygon : mPolygons) {
      polygon.remove();
    }
    mPolygons.clear();

    for (MapPlace mapPlace : places) {
      PolygonOptions polyOptions = new PolygonOptions();
      List<LatLng> points = new ArrayList<>();

      for (MapPlace.PolygonPoint location : mapPlace.getPolygon()) {
        points.add(new LatLng(location.getLat(), location.getLng()));
      }

      polyOptions.addAll(points);

      // get current map zone
      MapZone mapZone = mMapDataSingleton.getMapZones().get(mapPlace.getTitle());

      // if zone has data color it with the color based on the zone value
      if (hasZoneData(mapZone)) {
        String level = getMapZoneDataLevel(mapZone.getZoneData());
        AqiIndex aqiIndex = AqiIndex.getAQIForLevel(level);

        polyOptions.fillColor(
            ColorUtils.setAlphaComponent(ContextCompat.getColor(getContext(), aqiIndex.getFillColor()), mDefaultFillOpacity)
        );
        polyOptions.strokeColor(ContextCompat.getColor(getContext(), aqiIndex.getStrokeColor()));
        polyOptions.strokeWidth(4);
        polyOptions.geodesic(false);
        polyOptions.clickable(true);

        Polygon polygon = mMap.addPolygon(polyOptions);
        mPolygons.add(polygon);
        mPolyReadingMap.put(polygon.getId(), mapZone);
      } else {
        // otherwise set zone gray
        AqiIndex aqiIndex = AqiIndex.getAQIForLevel("");

        polyOptions.fillColor(
            ColorUtils.setAlphaComponent(ContextCompat.getColor(getContext(), aqiIndex.getFillColor()), mDefaultFillOpacity)
        );
        polyOptions.strokeColor(ContextCompat.getColor(getContext(), aqiIndex.getStrokeColor()));
        polyOptions.strokeWidth(4);
        polyOptions.geodesic(false);
        polyOptions.clickable(true);

        Polygon polygon = mMap.addPolygon(polyOptions);
        mPolyReadingMap.put(polygon.getId(), mapZone);
        mPolygons.add(polygon);
      }
    }
  }

  private boolean hasZoneData(MapZone mapZone) {
    if (mapZone == null || mapZone.getZoneData() == null || mapZone.getZoneData().size() == 0) {
      return false;
    }

    boolean hasZoneData = false;
    for (ZoneData zoneData : mapZone.getZoneData()) {
      if (zoneData.getValue() != -1) {
        hasZoneData = true;
      }
    }
    return hasZoneData;
  }

  private String getMapZoneDataLevel(RealmList<ZoneData> zoneData) {
    String airPM10Level = "";
    String airPM25Level = "";
    for (ZoneData zone : zoneData) {
      if (zone.getSensorId().equals(Constants.AIR_PM10)) {
        airPM10Level = zone.getLevel();
      } else if (zone.getSensorId().equals(Constants.AIR_PM2P5)) {
        airPM25Level = zone.getLevel();
      }
    }
    if (AqiIndex.getAQIForLevel(airPM25Level).getLevel() > AqiIndex.getAQIForLevel(airPM10Level).getLevel()) {
      return airPM25Level;
    }
    return airPM10Level;
  }

  private void createMarkers(List<Device> devices) {
    Log.d(TAG, "createMarkers()");
    mClusterManager.clearItems();
    for (Device device : devices) {
      if (device.isActive() || device.isMine()) {
        DeviceMap deviceMap = getDeviceMap(device);
        mClusterManager.addItem(deviceMap);
      }
    }

    mClusterManager.cluster();
    Log.d(TAG, "Wait");
  }

  private DeviceMap getDeviceMap(Device device) {
    return new DeviceMap(device.getLocation().getLat(), device.getLocation().getLng(), device);
  }

  private void showInfoWindow(Device device, AqiIndex aqiIndex, Double pm25Value, Double pm10Value, Double tempValue) {
    Log.d(TAG, "showInfoWindowWithActiveDevice()");
    mInfoFragment.updateValues(device, aqiIndex, pm25Value, pm10Value, tempValue, true, device.isActive());
    setInfoWindow();
  }

  private void showInfoWindow(MapZone mapZone, AqiIndex aqiIndex, Double pm25Value, Double pm10Value, Double tempValue) {
    Log.d(TAG, "showInfoWindow()");
    mInfoFragment.updateValues(mapZone, aqiIndex, pm25Value, pm10Value, tempValue, null);
    setInfoWindow();
  }

  private void setInfoWindow() {
    Log.d(TAG, "setInfoWindow()");
    if (mIsShown) {
      return;
    }

    if (mInfoFragment.getView().getVisibility() == View.GONE) {
      mInfoFragment.getView().setVisibility(View.VISIBLE);
    }

    mInfoFragment.getView().startAnimation(mSlideUpAnimation);
    mIsShown = true;
  }

  private void hideInfoWindow() {
    Log.d(TAG, "hideInfoWindow()");
    if (!mIsShown) {
      return;
    }

    if (mInfoFragment != null && mSlideDownAnimation != null && mInfoFragment.getView() != null) {
      mInfoFragment.getView().startAnimation(mSlideDownAnimation);
      mIsShown = false;
    }
  }

  @Override
  public boolean onClusterItemClick(DeviceMap deviceMap) {
    hideInfoWindow();

    Device device = deviceMap.getDevice();

    // if there was previously selected device deselect it
    if (mLastSelectedMarker != null && mLastSelectedDevice != null && device != mLastSelectedDevice) {
      Bitmap defaultIcon = DevicesMapUtils.getBitmapIcon(getContext(), mLastSelectedDevice);
      try {
        mLastSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultIcon));
      } catch (Exception ex) {
        if (!BuildConfig.DEBUG) {
          Crashlytics.log(Log.ERROR, TAG, ex.toString());
        }
        Log.e(TAG, ex.toString());
      }
    }
    // otherwise get icon for currently selected device
    else if (mLastSelectedMarker == null && mLastSelectedDevice != null) {
      Marker marker = mClusterRenderer.getMarker(deviceMap);
      if (marker != null) {
        Bitmap defaultIcon = DevicesMapUtils.getBitmapIcon(getContext(), mLastSelectedDevice);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultIcon));
      }
    }

    // remove marker pointer
    mLastSelectedMarker = null;

    if (mCurrentPolygon != null) {
      setPolygon(null);
    }

    Bitmap icon = DevicesMapUtils.getSelectedBitmapIcon(getContext(), device);
    Marker marker = mClusterRenderer.getMarker(deviceMap);
    // if marker from renderer is not null set it selected icon
    if (marker != null) {
      marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    // set last selected device marker and icon to the current one for select/deselect logic
    mLastSelectedDevice = device;
    mLastSelectedMarker = marker;

    String sensor = DevicesMapUtils.getSensor(device);
    Double temperature = null;
    // if there's temperature value get it otherwise we pass null to info window for layout to be laid out properly
    if (!device.getMapMeta().get(Constants.AIR_TEMPERATURE).getLevel().equals("")) {
      temperature = device.getMapMeta().get(Constants.AIR_TEMPERATURE).getValue();
    }
    showInfoWindow(device, device.getAQI(sensor),
        device.getMapMeta().get(Constants.AIR_PM2P5).getValue(),
        device.getMapMeta().get(Constants.AIR_PM10).getValue(),
        temperature);

    return true;
  }

  @Override
  public void infoWindowClicked(DeviceMeasurementCollection deviceMeasurementCollection) {
    Log.d(TAG, "infoWindowClicked(DeviceMeasurementCollection deviceMeasurementCollection)");
    hideInfoWindow();
    ((MainTabFragment) getParentFragment()).navigateToDeviceInfo(deviceMeasurementCollection.getDeviceId());
  }

  @Override
  public void infoWindowClicked(Device device) {
    Log.d(TAG, "infoWindowClicked(Device device)");
    hideInfoWindow();
    ((MainTabFragment) getParentFragment()).navigateToDeviceInfo(device.getIdString());
  }

  @Override
  public void setLoadingView(String message) {

  }

  @Override
  public void setContentView() {

  }

}
