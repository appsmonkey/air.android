package io.cityos.cityosair.ui.main;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.jetbrains.annotations.NotNull;

import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.GaugeConfig;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.ui.aqi.AQIActivity;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.ui.base.fragment.BasePagerFragment;
import io.cityos.cityosair.ui.main.adapter.ReadingsAdapter;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.app.CityOSDateUtils;
import io.cityos.cityosair.util.map.DevicesComparator;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainFragment extends BasePagerFragment implements MainView {

  @Inject MainPresenter mainPresenter;

  @BindView(R.id.map_fragment_container) FrameLayout fragmentContainer;
  @BindView(R.id.btn_menu) Button menuBtn;
  @BindView(R.id.tv_deviceName) AutofitTextView deviceNameTextView;
  @BindView(R.id.tv_lastUpdated) TextView lastUpdatedTextView;
  @BindView(R.id.progress_overlay) View progressOverlay;

  //Gauge Components
  @BindView(R.id.gauge_layout) RelativeLayout gaugeLayout;
  @BindView(R.id.arc_progress) ArcProgress gaugeProgress;
  @BindView(R.id.iv_status) ImageView gaugeStatusImageView;
  @BindView(R.id.iv_ribbon) ImageView gaugeRibbonImageView;
  @BindView(R.id.tv_gauge_status) TextView gaugeStatusTextView;

  //Loading view
  @BindView(R.id.iv_loading_gif) GifImageView loadingGifImageView;

  //Divider view
  @BindView(R.id.divider_view) View dividerView;

  //Swipe to refresh and recycler view components
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.readings_recycler_view) RecyclerView readingsRecyclerView;

  @BindString(R.string.device_changed) String strUpdated;
  @BindString(R.string.something_went_wrong) String strSomethingWentWrong;
  @BindString(R.string.no_data) String strNoData;

  private ReadingsAdapter adapter;
  boolean isMap = false;
  private Timer gaugeTimer;
  private AQIEnum currentAQI = AQIEnum.AIR_PM10;
  private GifDrawable gifDrawable;
  private DeviceMeasurementCollection deviceMeasurementCollection;
  private boolean isIndoor;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  private List<Device> mDevices;
  private String mMapDeviceId;

  static Function<Void, MainFragment> getFragmentCreator() {
    return aVoid -> new MainFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    gaugeTimer = new Timer();

    View view = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, view);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
    mainPresenter.setView(this);

    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(swipeRefreshLayout.getContext(), R.color.colorSmallButton));
    gifDrawable = (GifDrawable) loadingGifImageView.getDrawable();

    adapter = new ReadingsAdapter();
    readingsRecyclerView.setAdapter(adapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
    readingsRecyclerView.setLayoutManager(layoutManager);

    swipeRefreshLayout.setOnRefreshListener(() -> {
      if (mMapDeviceId != null || deviceMeasurementCollection != null) {
        if (mMapDeviceId != null && !mMapDeviceId.equals("")) {
          mainPresenter.updateForDeviceId(mMapDeviceId);
        } else {
          mainPresenter.updateForDeviceId(deviceMeasurementCollection.getDeviceId());
        }
      } else {
        mainPresenter.updateForDeviceName(deviceNameTextView.getText().toString());
      }
    });

    readingsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
        dividerView.setVisibility(firstVisibleItem == 0 ? View.INVISIBLE : View.VISIBLE);
      }
    });

    AutofitHelper.create(deviceNameTextView);
  }

  @Override
  public void onStart() {
    super.onStart();

    // on start set reset gauge control and get devices from api
    if (mDevices == null) {
      gaugeProgress.setProgress(0);
      gaugeRibbonImageView.setVisibility(View.INVISIBLE);
      gaugeStatusImageView.setVisibility(View.INVISIBLE);
      gaugeStatusTextView.setVisibility(View.INVISIBLE);
      gaugeLayout.setClickable(false);

      mainPresenter.getDevices();
    }
  }

  @Override
  public void devicesRefreshed(List<Device> devices) {
    // save loaded devices to fragment var
    mDevices = devices;
    // if device is selected from side menu `selectedDevice` will not be null since flow is from menu
    String selectedDevice = ((MainTabFragment) getParentFragment()).getSelectedDevice();
    // if `selectedDevice` is null this is main entry from login get default device per specs
    if (selectedDevice == null) {
      Device device = getMyDefaultDevice();
      // if user's default device is present get data or it
      if (device != null) {
        mainPresenter.updateForDeviceId(device.getDeviceId());
      } else {
        // otherwise get city air data
        mainPresenter.updateForDeviceId("");
      }
    } else {
      // if `selectedDevice` is not null we're in side menu flow get data for selected device
      mainPresenter.updateForDeviceId(selectedDevice);
    }
  }


  /*
    Get user's default device. Sorted by alphabet descending, if there is an outdoor the first one
    if not move to indoor devices, if not indoor devices return null. the logic that uses this method
    will call for city air
   */
  private Device getMyDefaultDevice() {
    List<Device> devices = CacheUtil.getSharedCache().getMyDevices();
    List<Device> indoorDevices = new ArrayList<>();
    List<Device> outdoorDevices = new ArrayList<>();
    for (Device device : devices) {
      if (device.isIndoor()) {
        indoorDevices.add(device);
      } else {
        if (!device.getName().equals(Constants.SARAJEVO_CITY_AIR)) {
          outdoorDevices.add(device);
        }
      }
    }

    Collections.sort(indoorDevices, new DevicesComparator());
    Collections.sort(outdoorDevices, new DevicesComparator());

    if (outdoorDevices.size() > 0) {
      return outdoorDevices.get(0);
    } else if (indoorDevices.size() > 0) {
      return indoorDevices.get(0);
    }

    return null;
  }

  @Override
  public void onFragmentShown() {
    super.onFragmentShown();
  }

  @OnClick(R.id.btn_menu)
  void menuClicked() {
    ((MainTabFragment) getParentFragment()).openMenuClicked(deviceNameTextView.getText().toString(), false);
  }

  @OnClick(R.id.btn_map)
  void mapClicked() {
    isMap = true;
    if (deviceMeasurementCollection != null) {
      ((MainTabFragment) getParentFragment()).navigateToMapFragment(deviceMeasurementCollection, isIndoor);
    }
  }

  @OnClick(R.id.gauge_layout)
  void gaugeClicked() {
    AQIActivity.show(getActivity(), currentAQI);
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return mainPresenter;
  }

  // Reset gauge control to initial values
  private void resetGauge() {
    gaugeProgress.setProgress(0);
    gaugeRibbonImageView.setVisibility(View.INVISIBLE);
    gaugeStatusImageView.setVisibility(View.INVISIBLE);
    gaugeStatusTextView.setVisibility(View.INVISIBLE);
    gaugeLayout.setClickable(false);
  }

  // Set gauge control with gauge config values determined on the gathered device data
  private void setGauge(final GaugeConfig gaugeConfig) {
    // if gauge timer is not null reset
    // clear disposable to remove garbage from the previous use
    if (gaugeTimer != null) {
      gaugeTimer.cancel();
      gaugeTimer.purge();
      compositeDisposable.clear();
    }

    gaugeRibbonImageView.setVisibility(View.VISIBLE);
    gaugeStatusImageView.setVisibility(View.VISIBLE);
    gaugeStatusTextView.setVisibility(View.VISIBLE);
    gaugeLayout.setClickable(true);

    // if there's no pm data set gauge control, ribbon to gray, with title 'No PM data'
    if (getResources().getString(gaugeConfig.getRibbonText()).equals(getResources().getString(R.string.aqi_zero_ribbon))) {
      Drawable ribbonImage = ContextCompat.getDrawable(getActivity(), gaugeConfig.getRibbonImage());
      ribbonImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.aqi_zero_gauge), PorterDuff.Mode.SRC_ATOP);
      gaugeProgress.setFinishedStrokeColor(ContextCompat.getColor(getActivity(), R.color.aqi_zero_gauge));
      gaugeProgress.setUnfinishedStrokeColor(ContextCompat.getColor(getActivity(), R.color.aqi_zero_gauge));
      gaugeRibbonImageView.setImageDrawable(ribbonImage);
      gaugeStatusImageView.setImageResource(android.R.color.transparent);
      gaugeStatusTextView.setText(gaugeConfig.getRibbonText());
    }
    // otherwise set gauge control, ribbon, and text respond to the received data
    else {
      Drawable ribbonImage = ContextCompat.getDrawable(getActivity(), gaugeConfig.getRibbonImage());
      ribbonImage.setColorFilter(ContextCompat.getColor(getContext(), gaugeConfig.getProgressColor()), PorterDuff.Mode.SRC_ATOP);
      gaugeProgress.setFinishedStrokeColor(ContextCompat.getColor(getActivity(), gaugeConfig.getProgressColor()));
      gaugeProgress.setUnfinishedStrokeColor(ContextCompat.getColor(getActivity(), R.color.graph_line));
      gaugeRibbonImageView.setImageDrawable(ribbonImage);
      gaugeStatusImageView.setImageResource(gaugeConfig.getCenterImage());
      gaugeStatusTextView.setText(gaugeConfig.getRibbonText());
    }

    // set gauge progress bar to max and 0 and animate up to the value from gauge config
    gaugeProgress.setMax(gaugeConfig.getMaxValue());
    gaugeProgress.setProgress(0);
    compositeDisposable.add(Observable.interval(30, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(aLong -> {
          if (gaugeProgress.getProgress() < gaugeConfig.getProgressValue()) {
            gaugeProgress.setProgress(gaugeProgress.getProgress() + 1);
          } else {
            gaugeTimer.cancel();
            gaugeTimer.purge();
          }
        }).subscribeWith(new DisposableObserver<Long>() {
          @Override public void onNext(Long aLong) {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        }));
  }

  @Override public void onStop() {
    compositeDisposable.clear();
    super.onStop();
  }

  @Override public void onDestroyView() {
    compositeDisposable.dispose();
    super.onDestroyView();
  }

  @Override
  public void setContentForMeasurement(DeviceMeasurementCollection deviceMeasurementCollection) {
    mMapDeviceId = "";

    swipeRefreshLayout.setRefreshing(false);

    // set tuple for gauge config and aqi type
    Pair<GaugeConfig, AQIEnum> pair = GaugeConfig.getConfigForValue(
        deviceMeasurementCollection.getValueForReadingType(ReadingTypeEnum.AIR_PM10),
        deviceMeasurementCollection.getValueForReadingType(ReadingTypeEnum.AIR_PM2P5)
    );

    currentAQI = pair.second;

    // if there're device's data set controls and call set gauge control method with gauge config
    if (deviceMeasurementCollection.getMeasurements() != null) {
      adapter.updateDataSet(deviceMeasurementCollection.getMeasurements(), currentAQI, deviceMeasurementCollection.getDeviceId());
      readingsRecyclerView.setVisibility(View.VISIBLE);
      deviceNameTextView.setText(deviceMeasurementCollection.getDeviceName());
      lastUpdatedTextView.setText(String
          .format(strUpdated, CityOSDateUtils.getLastUpdatedDateString(deviceMeasurementCollection.getTimestamp())));
      setGauge(pair.first);
    }
    // otherwise reset controls
    else {
      lastUpdatedTextView.setText(strNoData);
      deviceNameTextView.setText(deviceMeasurementCollection.getDeviceName());
      readingsRecyclerView.setVisibility(View.GONE);
      resetGauge();
    }

    readingsRecyclerView.smoothScrollToPosition(0);
  }

  @Override
  public void setContentForDevice(DeviceMeasurementCollection deviceMeasurementCollection, Device device) {
    mMapDeviceId = "";
    // set selected device in side menu if not already selected
    ((MainTabFragment) getParentFragment()).setSelectedDevice(device.getIdString());

    this.deviceMeasurementCollection = deviceMeasurementCollection;
    this.isIndoor = device.isIndoor();

    List<Device> devices = CacheUtil.getSharedCache().getMyDevices();
    Device myDevice = null;
    for (Device dev : devices) {
      if (dev.getName().equals(deviceMeasurementCollection.getDeviceName())) {
        myDevice = dev;
      }
    }

    if (myDevice != null) {
      ((MainTabFragment) getParentFragment()).devicesRefreshed(deviceMeasurementCollection.getDeviceName());
    }

    swipeRefreshLayout.setRefreshing(false);

    // set tuple for gauge config and aqi type
    Pair<GaugeConfig, AQIEnum> pair = GaugeConfig.getConfigForValue(
        deviceMeasurementCollection.getValueForReadingType(ReadingTypeEnum.AIR_PM10),
        deviceMeasurementCollection.getValueForReadingType(ReadingTypeEnum.AIR_PM2P5)
    );

    currentAQI = pair.second;

    // if there're device's data set controls and call set gauge control method with gauge config
    if (deviceMeasurementCollection.getMeasurements() != null) {
      adapter.updateDataSet(deviceMeasurementCollection.getMeasurements(), currentAQI, deviceMeasurementCollection.getDeviceId());
      readingsRecyclerView.setVisibility(View.VISIBLE);
      deviceNameTextView.setText(deviceMeasurementCollection.getDeviceName());
      lastUpdatedTextView.setText(String
              .format(strUpdated, CityOSDateUtils.getLastUpdatedDateString(deviceMeasurementCollection.getTimestamp())));
      setGauge(pair.first);
    }
    // otherwise reset controls
    else {
      lastUpdatedTextView.setText(strNoData);
      deviceNameTextView.setText(device.getName());
      readingsRecyclerView.setVisibility(View.GONE);
      resetGauge();
    }

    readingsRecyclerView.smoothScrollToPosition(0);
  }

  @Override
  public void setCityAir() {
    loadingGifImageView.setVisibility(View.GONE);
    gifDrawable.setVisible(false, true);
    gaugeLayout.setVisibility(View.VISIBLE);
    ((MainTabFragment) getParentFragment()).navigateToCityInfo();
  }

  @Override
  public void setContentLoading() {
    loadingGifImageView.setVisibility(View.VISIBLE);
    gifDrawable.reset();
    gifDrawable.setVisible(true, true);
    gaugeLayout.setVisibility(View.INVISIBLE);
  }

  @Override
  public void removeContentLoading() {
    loadingGifImageView.setVisibility(View.GONE);
    gifDrawable.setVisible(false, true);
    gaugeLayout.setVisibility(View.VISIBLE);
  }

  void updateDevice(String deviceId) {
    mMapDeviceId = deviceId;
    mainPresenter.updateForDeviceId(deviceId);
  }

  void updateDeviceByName(String deviceName) {
    mainPresenter.updateForDeviceName(deviceName);
  }

  @Override
  public void setLoadingView(String message) {

  }

  @Override
  public void setContentView() {

  }
}
