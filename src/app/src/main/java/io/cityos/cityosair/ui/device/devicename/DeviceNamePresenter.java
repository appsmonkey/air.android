package io.cityos.cityosair.ui.device.devicename;

import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.data.datasource.datafetch.AddDeviceDataFetch;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;
import javax.inject.Inject;

public class DeviceNamePresenter extends BasePresenter<DeviceNameView> {

  private AddDeviceDataFetch addDeviceDataFetch;

  @Inject DeviceNamePresenter(AddDeviceDataFetch addDeviceDataFetch) {
    this.addDeviceDataFetch = addDeviceDataFetch;
  }

  // add device call to api once the last screen in the add device flow is set
  void addDevice(AddDevicePayload addDevicePayload) {
    getCompositeDisposable().add(
        addDeviceDataFetch.fetch(addDevicePayload).subscribeWith(new DisposableObserver<Object>() {
          @Override public void onNext(Object o) {
            getView().setContentView();
            getView().deviceAdded(addDevicePayload.getToken());
          }

          @Override public void onError(Throwable e) {
            getView().setContentView();
            getView().addDeviceError();
          }

          @Override public void onComplete() {

          }
        }));
  }
}
