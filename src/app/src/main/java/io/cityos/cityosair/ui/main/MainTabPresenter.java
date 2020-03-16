package io.cityos.cityosair.ui.main;

import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.datasource.datafetch.DevicesDataFetch;
import io.cityos.cityosair.ui.base.presenter.PagerPresenter;
import io.reactivex.observers.DisposableObserver;
import java.util.List;
import javax.inject.Inject;

public class MainTabPresenter extends PagerPresenter<MainTabView> {

  private DevicesDataFetch devicesDataFetch;

  @Inject MainTabPresenter(DevicesDataFetch devicesDataFetch) {
    this.devicesDataFetch = devicesDataFetch;
  }

  void refreshDevicesClicked() {
    getCompositeDisposable().add(devicesDataFetch.fetch(null).subscribeWith(new DevicesObserver()));
  }

  private class DevicesObserver extends DisposableObserver<List<Device>> {

    @Override public void onNext(List<Device> devices) {
      getView().devicesRefreshed();
    }

    @Override public void onError(Throwable e) {

    }

    @Override public void onComplete() {

    }
  }
}
