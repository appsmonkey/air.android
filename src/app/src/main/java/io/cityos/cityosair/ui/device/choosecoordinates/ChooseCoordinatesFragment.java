package io.cityos.cityosair.ui.device.choosecoordinates;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.data.model.Coordinates;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.device.devicename.DeviceNameFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.cache.CacheUtil;

public class ChooseCoordinatesFragment extends NewBaseFragment implements OnMapReadyCallback {
  private static String TAG = ChooseCoordinatesFragment.class.getSimpleName();

  private GoogleMap map;
  private ArrayList<Marker> addedMarkers;
  private LatLng latLng;
  Geocoder geocoder = new Geocoder(CityOSAirApplication.getAppContext(), Locale.ENGLISH);
  // private String city = "Sarajevo";

  @BindView(R.id.button_choose_location) Button btnAddDevice;
  @BindView(R.id.choose_coordinates_map) MapView mapView;

  @BindString(R.string.choose_device_location_continue) String strContinueAddDevice;

  @OnClick(R.id.button_choose_location)
  void onAddDeviceClicked() {

    AddDevicePayload addDevicePayload = CacheUtil.getSharedCache().getAddDevicePayload();
    addDevicePayload.setCoordinates(new Coordinates(latLng.latitude, latLng.longitude));
    // addDevicePayload.setCity(city);
    CacheUtil.getSharedCache().saveAddDevicePayload(addDevicePayload);

    FragmentManager manager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    transaction.replace(R.id.a_layout, new DeviceNameFragment());
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    addedMarkers = new ArrayList<>();

    btnAddDevice.setEnabled(false);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
  }

  @Override public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override public void onDestroyView() {
    mapView.onDestroy();
    super.onDestroyView();
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override protected int getFragmentLayoutId() {
    return R.layout.fragment_choose_coordinates;
  }

  @Override protected BasePresenter getFragmentPresenter() {
    return null;
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    this.map = googleMap;

    // Set City Map coordinates to Sarajevo. Refactor once cities are solved on backend
    LatLng location = new LatLng(43.852329, 18.395026);
    CameraUpdate cameraPosition = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(location, 13));
    map.moveCamera(cameraPosition);
    map.setOnMapClickListener(this::placeMarker);
  }

  private void placeMarker(LatLng latLng) {
    this.latLng = latLng;

//    try {
//      List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//      if (addresses != null && addresses.size() > 0) {
//        Address address = addresses.get(0);
//        Log.i(TAG, "get premises: " + address.getPremises());
//        Log.i(TAG, "get thoroughfare: " + address.getThoroughfare());
//        Log.i(TAG, "get sub thoroughfare: " + address.getSubThoroughfare());
//        Log.i(TAG, "get premises: " + address.getPremises());
//        Log.i(TAG, "admin area: " + address.getAdminArea());
//        Log.i(TAG, "sub admin area: " + address.getSubAdminArea());
//        Log.i(TAG, "locality: " + address.getLocality());
//        Log.i(TAG, "sub locality: " + address.getSubLocality());
//        Log.i(TAG, "featur ename: " + address.getFeatureName());
//        city = address.getSubAdminArea();
//        if (city == null || city.equals("")) {
//          city = Constants.SARAJEVO_CITY;
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    btnAddDevice.setEnabled(true);
    btnAddDevice.setText(strContinueAddDevice);

    if (addedMarkers.size() > 0) {
      for (Marker marker : addedMarkers) {
        marker.remove();
      }
    }

    Marker marker = map.addMarker(new MarkerOptions()
        .position(latLng));

    addedMarkers.add(marker);
  }
}
