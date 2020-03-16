package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class AddDeviceDataFetch extends AbstractDataFetch<Object, AddDevicePayload> {

  DevicesDataFetch devicesDataFetch;

  @Inject AddDeviceDataFetch(DevicesDataFetch devicesDataFetch) {
    this.devicesDataFetch = devicesDataFetch;
  }

  @Override protected Observable<Object> fetchData(AddDevicePayload addDevicePayload) {
    return api.getClient().addDevice(addDevicePayload).flatMap(objectBase ->
        devicesDataFetch.fetchData(false));
  }
}
