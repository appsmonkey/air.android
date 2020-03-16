package io.cityos.cityosair.ui.main;

import java.util.List;

import io.cityos.cityosair.data.datasource.datafetch.DevicesDataFetch;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.messages.requests.DeviceIdPayload;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.datafetch.GetLatestDataFetch;
import io.cityos.cityosair.ui.base.presenter.PagerPresenter;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import javax.inject.Inject;

public class MainPresenter extends PagerPresenter<MainView> {

  private GetLatestDataFetch getLatestDataFetch;
  private DevicesDataFetch devicesDataFetch;

  @Inject
  MainPresenter(DevicesDataFetch devicesDataFetch, GetLatestDataFetch getLatestDataFetch) {
    this.devicesDataFetch = devicesDataFetch;
    this.getLatestDataFetch = getLatestDataFetch;
  }

  // get latest measurement data for device with this id
  void updateForDeviceId(String deviceId) {
    DeviceIdPayload deviceIdPayload = new DeviceIdPayload(deviceId);

    getView().setContentLoading();
    getLatestDataFetch.fetch(deviceIdPayload)
        .subscribe(new LatestDeviceMeasurementObserver(deviceId));
  }

  // call on create view with the default device name
  void updateForDeviceName(String deviceName) {
    Device device = CacheUtil.getSharedCache().getDeviceByName(deviceName);
    // if device name is not in local storage update for city air
    if (device == null) {
      updateForDeviceId(deviceName);
    } else {
      // otherwise get the latest measurements for device with this device id
      DeviceIdPayload deviceIdPayload = new DeviceIdPayload(device.getIdString());
      getView().setContentLoading();
      getLatestDataFetch.fetch(deviceIdPayload)
              .subscribe(new LatestDeviceMeasurementObserver(device.getIdString()));
    }
  }

  private class LatestDeviceMeasurementObserver implements Observer<DeviceMeasurementCollection> {
    String deviceId;

    LatestDeviceMeasurementObserver(String deviceId) {
      this.deviceId = deviceId;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(DeviceMeasurementCollection deviceMeasurementCollection) {
      Device device = CacheUtil.getSharedCache().getDeviceById(deviceId);
      if (getView() != null) {
        getView().removeContentLoading();
        if (device != null) {
          getView().setContentForDevice(deviceMeasurementCollection, device);
        } else if (deviceMeasurementCollection != null) {
          getView().setContentForMeasurement(deviceMeasurementCollection);
        } else {
          getView().setCityAir();
        }
      }
    }

    @Override
    public void onError(Throwable e) {
      if (getView() != null) {
        getView().removeContentLoading();
        DeviceMeasurementCollection deviceMeasurementCollection = getCachedCollection(deviceId);
        if (deviceMeasurementCollection != null) {
          Device device = CacheUtil.getSharedCache().getDeviceById(deviceId);
          if (device != null) {
            getView().setContentForDevice(deviceMeasurementCollection, device);
          } else {
            getView().setCityAir();
          }
        } else {
          getView().setCityAir();
        }
      }
    }

    @Override public void onComplete() {

    }
  }

  void getDevices() {
    getCompositeDisposable().add(devicesDataFetch.fetch(null).subscribeWith(new DevicesObserver()));
  }

  private class DevicesObserver extends DisposableObserver<List<Device>> {

    @Override public void onNext(List<Device> devices) {
      getView().devicesRefreshed(devices);
    }

    @Override public void onError(Throwable e) {

    }

    @Override public void onComplete() {

    }
  }

  private DeviceMeasurementCollection getCachedCollection(String deviceId) {
    return CacheUtil.getSharedCache().getDeviceMeasurementCollection(deviceId);
  }
}
