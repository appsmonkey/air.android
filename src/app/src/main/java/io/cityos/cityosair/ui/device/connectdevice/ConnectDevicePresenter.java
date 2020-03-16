package io.cityos.cityosair.ui.device.connectdevice;

import io.cityos.cityosair.data.model.DeviceIdEntity;
import io.cityos.cityosair.data.datasource.datafetch.GetDeviceIdDataFetch;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;
import javax.inject.Inject;

public class ConnectDevicePresenter extends BasePresenter<ConnectDeviceView> {

  private GetDeviceIdDataFetch getDeviceIdDataFetch;

  @Inject ConnectDevicePresenter(GetDeviceIdDataFetch getDeviceIdDataFetch) {
    this.getDeviceIdDataFetch = getDeviceIdDataFetch;
  }

  // get device id (thing_name) from http://192.168.4.1/id
  public void getDeviceId() {
    getView().setLoadingView(null);

    getCompositeDisposable().add(
        getDeviceIdDataFetch.fetch(null).subscribeWith(new DisposableObserver<DeviceIdEntity>() {

          @Override public void onNext(DeviceIdEntity deviceIdEntity) {
            getView().deviceIdFetched(deviceIdEntity.getThingName());
            getView().setContentView();
          }

          @Override public void onError(Throwable e) {
            getView().setContentView();
          }

          @Override public void onComplete() {

          }
        })
    );
  }
}
