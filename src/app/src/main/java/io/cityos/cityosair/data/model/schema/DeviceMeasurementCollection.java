package io.cityos.cityosair.data.model.schema;

import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeviceMeasurementCollection extends RealmObject {

  @PrimaryKey
  private String deviceId;
  private String deviceName;
  private long timestamp;
  private RealmList<DeviceMeasurement> measurements;

  public DeviceMeasurementCollection() {
  }

  public DeviceMeasurementCollection(RealmList<DeviceMeasurement> measurements) {
    this.measurements = measurements;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public DeviceMeasurementCollection setDeviceName(String deviceName) {
    this.deviceName = deviceName;
    return this;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public DeviceMeasurementCollection setDeviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public DeviceMeasurementCollection setTimestamp(long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public RealmList<DeviceMeasurement> getMeasurements() {
    return measurements;
  }

  public Double getValueForReadingType(ReadingTypeEnum readingType) {

    if (measurements != null) {
      for (DeviceMeasurement measurement : measurements) {
        if (measurement.getReadingType().getEnumValue() == readingType) {
          return measurement.getValue();
        }
      }
    }

    return null;
  }
}
