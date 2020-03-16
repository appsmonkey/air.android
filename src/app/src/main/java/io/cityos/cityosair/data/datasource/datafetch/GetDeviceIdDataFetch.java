package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.model.DeviceIdEntity;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.util.network.RetryWithDelay;
import io.reactivex.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;

public class GetDeviceIdDataFetch extends AbstractDataFetch<DeviceIdEntity, Void> {

  @Inject GetDeviceIdDataFetch() {
  }

  @Override protected Observable<DeviceIdEntity> fetchData(Void payload) {
    AtomicInteger ai = new AtomicInteger(0);
    return api.getClient()
        .getDeviceId("http://192.168.4.1/id")
        .retryWhen(new RetryWithDelay(10, 2000));
  }
}
