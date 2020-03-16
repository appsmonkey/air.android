package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapData;
import io.cityos.cityosair.data.model.map.MapZone;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.ui.map.MapDataSingleton;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class MapDataFetch extends AbstractDataFetch<MapData, String> {

  MapDataSingleton mapDataSingleton;

  @Inject MapDataFetch(MapDataSingleton mapDataSingleton) {
    this.mapDataSingleton = mapDataSingleton;
  }

  @Override protected Observable<MapData> fetchData(String filter) {
      // AIR_PM10,AIR_PM2P5&device_data=air_pm10
    return api.getClient().getMapDevices(Constants.MAP_READING_SENSOR, Constants.MAP_READING_DATA, filter)
        .map(Base::getData)
        .subscribeOn(Schedulers.io())
        .doOnNext(mapData -> {
          List<Device> devices = mapData.getDevices();
          Map<String, MapZone> zoneDataMap = new HashMap<>();


          for (Device device : devices) {
            device.setMapMetas(device.getMapMeta());
            device.setMapDevice(true);
          }

          for (MapZone mapZone : mapData.getZones()) {
            zoneDataMap.put(mapZone.getZoneName(), mapZone);
          }

          // CacheUtil.getSharedCache().deleteAllZoneData();
          CacheUtil.getSharedCache().save(devices);
          CacheUtil.getSharedCache().save(mapData.getZones());

          mapDataSingleton.setDeviceList(devices);
          mapDataSingleton.setMapZones(zoneDataMap);
        })
        .observeOn(AndroidSchedulers.mainThread());
  }
}
