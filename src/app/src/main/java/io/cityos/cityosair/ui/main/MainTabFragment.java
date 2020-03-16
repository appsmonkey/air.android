package io.cityos.cityosair.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.ui.aqi.AQIActivity;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.ui.map.MapPresenter;
import io.cityos.cityosair.util.map.DevicesComparator;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.ui.device.connectdevice.ConnectDeviceActivity;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.ui.base.fragment.BaseFragment;
import io.cityos.cityosair.ui.base.fragment.BaseTabFragment;
import io.cityos.cityosair.ui.base.fragment.OnPageChangeListener;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.ui.main.adapter.CustomFragmentStatePagerAdapter;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.map.MapFragment;
import io.cityos.cityosair.ui.settings.SettingsActivity;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class MainTabFragment extends BaseTabFragment implements MainTabView {
  private static String TAG = MainTabFragment.class.getSimpleName();

  @Inject MainTabPresenter mainTabPresenter;
  @Inject HttpLoggingInterceptor httpLoggingInterceptor;

  private CustomFragmentStatePagerAdapter pagerAdapter;
  private String selectedDevice;

  @BindView(R.id.pager) ViewPager viewPager;
  @BindView(R.id.drawer_layout) DrawerLayout drawerlayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;
  private String selectedDeviceName;
  private boolean isMap;

  @OnClick(R.id.btn_menu_close)
  void menuCloseClicked() {
    drawerlayout.closeDrawer(navigationView);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
    mainTabPresenter.setView(this);

    pagerAdapter = new CustomFragmentStatePagerAdapter(getChildFragmentManager());
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupToolbar();

    List<? extends Function<Void, ? extends BaseFragment>> fragmentCreators =
        Arrays.asList(MainFragment.getFragmentCreator(), MapFragment.getFragmentCreator());

    pagerAdapter.setFragmentCreators(fragmentCreators);
    viewPager.setAdapter(pagerAdapter);
    viewPager.setOffscreenPageLimit(fragmentCreators.size());
  }

  @Override
  public void onStarting() {

  }

  @Override
  public void onResume() {
    super.onResume();

    setUpMenu(navigationView, selectedDeviceName, isMap);

    mainTabPresenter.refreshDevicesClicked();
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return mainTabPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_main_tab;
  }

  @Override
  public CustomFragmentStatePagerAdapter getPagerAdapter() {
    return pagerAdapter;
  }

  @Override
  public ViewPager getViewPager() {
    return viewPager;
  }

  @Override
  public OnPageChangeListener getOnPageChangeListenerWithLifecycle(int currentPosition) {
    return new OnPageChangeListener(pagerAdapter, viewPager, currentPosition);
  }

  public void openMenuClicked(String deviceName, boolean isMap) {

    this.selectedDeviceName = deviceName;
    this.isMap = isMap;

    setUpMenu(navigationView, deviceName, isMap);

    drawerlayout.openDrawer(navigationView);
  }

  // map button clicked on main screen, go to map with default device selected or city air
  // if there's a device on main dashboard map will zoom on it and open info view
  void navigateToMapFragment(DeviceMeasurementCollection deviceMeasurementCollection, boolean isIndoor) {
    viewPager.setCurrentItem(1);
    if (deviceMeasurementCollection.getDeviceId() != null && !deviceMeasurementCollection.getDeviceId().equals("0")) {
      ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).showDeviceDetails(deviceMeasurementCollection, isIndoor);
    }
  }

  void setSelectedDevice(String deviceId) {
    this.selectedDevice = deviceId;
  }

  String getSelectedDevice() {
    return selectedDevice;
  }

  // set map centered on city info, zoomed out on city, and go to city air dashboard
  public void navigateToCityInfo() {
    ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).centerMapOnCityAndSetDefaultZoom();
    viewPager.setCurrentItem(0);
    ((MainFragment) pagerAdapter.instantiateItem(viewPager, 0)).updateDevice("");
  }

  // navigate to device's dahsboard
  public void navigateToDeviceInfo(String deviceId) {
    viewPager.setCurrentItem(0);
    ((MainFragment) pagerAdapter.instantiateItem(viewPager, 0)).updateDevice(deviceId);
  }

  private void setupToolbar() {
    navigationView.setNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.menu_item_login:
          LoginActivity.show(getActivity());
          return true;
        case R.id.menu_item_pm2_5:
          AQIActivity.show(getActivity(), AQIEnum.AIR_PM2P5);
          return true;
        case R.id.menu_item_pm10:
          AQIActivity.show(getActivity(), AQIEnum.AIR_PM10);
          return true;
        case R.id.menu_item_settings:
          SettingsActivity.show(getActivity());
          return true;
        case R.id.menu_item_add_device:
          ConnectDeviceActivity.show(this);
          return true;
        case R.id.menu_item_device_refresh:
          mainTabPresenter.refreshDevicesClicked();
          drawerlayout.closeDrawer(navigationView);
          return true;
        default:
          ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).clearMap();
          if (item.getGroupId() == R.id.top_group) {

            //zarko.runjevac 31.1.2017 if any device is selected, remove selection
            Menu menu = navigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
              MenuItem itm = menu.getItem(i);
              if (itm.isChecked()) {
                itm.setChecked(false);
              }
            }

            item.setChecked(true);
            String deviceName = item.getTitle().toString();

            switch (deviceName) {
              case "Air Map":
                ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).mDevicesFilter = MapPresenter.MAP_FILTER.OUTDOOR;
                ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).centerMapOnCityAndCreateMarkers();
                viewPager.setCurrentItem(1);
                break;
              case "Sarajevo Air":
                ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).centerMapOnCityAndSetDefaultZoom();
                viewPager.setCurrentItem(0);
                ((MainFragment) pagerAdapter.instantiateItem(viewPager, 0)).updateDevice("");
                break;
              default:
                viewPager.setCurrentItem(0);
                ((MainFragment) pagerAdapter.instantiateItem(viewPager, 0)).updateDeviceByName(deviceName);
                break;
            }

            drawerlayout.closeDrawer(navigationView);
          }

          return false;
      }
    });
  }

  private void setUpMenu(NavigationView navigationView, String deviceName, boolean isMap) {
    Menu menu = navigationView.getMenu();

    User user = CacheUtil.getSharedCache().getUser();

    if (user != null && user.isGuest()) {
      menu.findItem(R.id.menu_item_login).setVisible(true);
    } else {
      menu.findItem(R.id.menu_item_login).setVisible(false);
    }

    if (user != null && !user.isGuest()) {
      menu.findItem(R.id.menu_item_add_device).setVisible(true);
    } else {
      menu.findItem(R.id.menu_item_add_device).setVisible(false);
    }

    if (user != null && !user.isGuest()) {
      menu.findItem(R.id.menu_item_device_refresh).setVisible(true);
    } else {
      menu.findItem(R.id.menu_item_device_refresh).setVisible(false);
    }

    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_add_device));
    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_login));
    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_pm10));
    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_pm2_5));
    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_settings));
    applyFontAndColorToMenuItem(menu.findItem(R.id.menu_item_device_refresh));

    menu.removeGroup(R.id.top_group);

    // get user's devices, sort them per specs (outdoor first, alphabetically, indoor second alphabetically)
    List<Device> devices = CacheUtil.getSharedCache().getMyDevices();
    if (devices != null && devices.size() > 0) {
      List<Device> sortedDevices = getSortedDevices(devices);
      for (Device device : sortedDevices) {
        String title = new SpannableStringBuilder(device.getName()).toString();
        if (title.isEmpty()) {
          title = device.getDeviceId();
        }
        navigationView.getMenu()
            .add(R.id.top_group, Menu.NONE, 0, title)
            .setChecked(device.getName().equals(deviceName) && !isMap);
      }
    }

    // add item to go to map screen at the end
    SpannableStringBuilder title = new SpannableStringBuilder("Air Map");
    navigationView.getMenu().add(R.id.top_group, Menu.NONE, 0, title).setChecked(isMap);
  }

  private List<Device> getSortedDevices(List<Device> devices) {
    List<Device> sortedDevices = new ArrayList<>();
    List<Device> indoorSortedDevices = new ArrayList<>();
    List<Device> outdoorSortedDevices = new ArrayList<>();

    for (Device device : devices) {
      if (device.isIndoor() && device.isMine() && !device.getName().equals(Constants.SARAJEVO_CITY_AIR)) {
        indoorSortedDevices.add(device);
      }
    }

    for (Device device : devices) {
      if (!device.isIndoor() && device.isMine() && !device.getName().equals(Constants.SARAJEVO_CITY_AIR)) {
        outdoorSortedDevices.add(device);
      }
    }

    Collections.sort(indoorSortedDevices, new DevicesComparator());
    Collections.sort(outdoorSortedDevices, new DevicesComparator());


    for (Device device : devices) {
      if (device.getName().equals(Constants.SARAJEVO_CITY_AIR)) {
        indoorSortedDevices.add(device);
      }
    }

    sortedDevices.addAll(outdoorSortedDevices);
    sortedDevices.addAll(indoorSortedDevices);

    return sortedDevices;
  }

  private void applyFontAndColorToMenuItem(MenuItem item) {
    SpannableStringBuilder title = new SpannableStringBuilder(item.getTitle());
    title.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorMenuBottomItems)), 0, title.length(), 0);

    item.setTitle(title);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == Constants.SETTINGS_REQUEST_CODE && data != null && resultCode == 1) {
      setSelectedDevice(data.getStringExtra(Constants.DEVICE_TOKEN));
      drawerlayout.closeDrawer(navigationView);
      ((MainFragment) pagerAdapter.instantiateItem(viewPager, 0)).updateDevice(getSelectedDevice());
      ((MapFragment) pagerAdapter.instantiateItem(viewPager, 1)).reloadMap();
    }
  }

  @Override
  public void devicesRefreshed() {
    setUpMenu(navigationView, selectedDeviceName, isMap);
  }

  void devicesRefreshed(String deviceName) {
    setUpMenu(navigationView, deviceName, isMap);
  }
}
