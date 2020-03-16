package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;

public class DevicesDataFetch extends AbstractDataFetch<List<Device>, Boolean> {

  SchemaDataFetch schemaDataFetch;
  GuestSchemaDataFetch guestSchemaDataFetch;

  @Inject DevicesDataFetch(SchemaDataFetch schemaDataFetch,
      GuestSchemaDataFetch guestSchemaDataFetch) {
    this.schemaDataFetch = schemaDataFetch;
    this.guestSchemaDataFetch = guestSchemaDataFetch;
  }

  @Override protected Observable<List<Device>> fetchData(Boolean isGuest) {
    return api.getClient().getUserDevices()
        .map(Base::getData)
        .doOnNext(devices -> {
          CacheUtil.getSharedCache().deleteAllDevices();
          CacheUtil.getSharedCache().save(devices);
        });
  }
}
