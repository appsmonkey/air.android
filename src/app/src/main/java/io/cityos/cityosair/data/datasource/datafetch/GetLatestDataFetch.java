package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.messages.requests.DeviceIdPayload;
import io.cityos.cityosair.data.model.LatestMeasurement;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.data.model.schema.GlobalSchemaReading;
import io.cityos.cityosair.data.model.schema.Step;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.util.NumberUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class GetLatestDataFetch extends
    AbstractDataFetch<DeviceMeasurementCollection, DeviceIdPayload> {

  NumberUtils numberUtils;

  @Inject GetLatestDataFetch(NumberUtils numberUtils) {
    this.numberUtils = numberUtils;
  }

  @Override
  protected Observable<DeviceMeasurementCollection> fetchData(DeviceIdPayload deviceIdPayload) {

    return api.getClient().getLatestForDevice(deviceIdPayload.getToken())
        .map(latestMeasurementBase -> {
          LatestMeasurement latestMeasurement = latestMeasurementBase.getData();

          List<DeviceMeasurement> measurements = new ArrayList<>();

          if (latestMeasurement.getLatest() != null) {
            for (Map.Entry<String, Double> entry : latestMeasurement.getLatest().entrySet()) {
              DeviceMeasurement measurement = new DeviceMeasurement();
              GlobalSchemaReading globalSchemaReading = CacheUtil.getSharedCache()
                  .getGlobalSchemaReading(entry.getKey());

              if (globalSchemaReading != null) {
                measurement.setName(globalSchemaReading.getName());
                measurement.setUnit(globalSchemaReading.getUnit());
                measurement.setSensorId(globalSchemaReading.getId());
                measurement.setValue(entry.getValue());
                measurement.setFormattedValue(numberUtils.format(entry.getValue(), 2));

                if (globalSchemaReading.getSteps() != null
                    && globalSchemaReading.getSteps().size() > 0) {
                  for (Step step : globalSchemaReading.getSteps()) {
                    if (measurement.getValue() >= step.getFrom()
                        && measurement.getValue() <= step.getTo()) {
                      measurement.setAverage(step.getResult());
                      break;
                    }
                  }
                }
              }

              measurements.add(measurement);
            }
          }

          DeviceMeasurementCollection deviceMeasurementCollection =
                  new DeviceMeasurementCollection(new RealmList<>(measurements.toArray(new DeviceMeasurement[measurements.size()])));
          deviceMeasurementCollection.setTimestamp(latestMeasurement.getTimestamp());
          deviceMeasurementCollection.setDeviceId(deviceIdPayload.getToken());
          deviceMeasurementCollection.setDeviceName(latestMeasurement.getName());

          return deviceMeasurementCollection;
        }).doOnNext(deviceMeasurementCollection -> {
          CacheUtil.getSharedCache().save(deviceMeasurementCollection);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}