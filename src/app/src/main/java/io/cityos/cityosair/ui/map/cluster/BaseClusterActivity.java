package io.cityos.cityosair.ui.map.cluster;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import io.cityos.cityosair.R;

public abstract class BaseClusterActivity extends FragmentActivity implements OnMapReadyCallback {
  private GoogleMap mMap;

  protected int getLayoutId() {
    return R.layout.map;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    setUpMap();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMap();
  }

  @Override
  public void onMapReady(GoogleMap map) {
    if (mMap != null) {
      return;
    }
    mMap = map;
    startTest();
  }

  private void setUpMap() {
    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
  }

  protected abstract void startTest();

  protected GoogleMap getMap() {
    return mMap;
  }
}