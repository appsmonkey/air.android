package io.cityos.cityosair.data.model.schema;

import android.os.Parcel;
import android.os.Parcelable;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.reading.ReadingType;
import io.realm.RealmObject;

@ParcelablePlease
public class DeviceMeasurement extends RealmObject implements Parcelable {

  int id;
  String sensorId;
  String name;
  String unit;
  String average;
  double value;
  String formattedValue;
  ReadingType readingType;

  public DeviceMeasurement() {
  }

  public String getFormattedValue() {
    return formattedValue;
  }

  public DeviceMeasurement setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
    return this;
  }

  public String getSensorId() {
    return sensorId;
  }

  public DeviceMeasurement setSensorId(String sensorId) {
    this.sensorId = sensorId;
    return this;
  }

  public double getValue() {
    return value;
  }

  public DeviceMeasurement setValue(double value) {
    this.value = value;
    return this;
  }

  public int getId() {
    return id;
  }

  public DeviceMeasurement setId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public DeviceMeasurement setName(String name) {
    this.name = name;
    return this;
  }

  public String getUnit() {
    return unit;
  }

  public DeviceMeasurement setUnit(String unit) {
    this.unit = unit;
    return this;
  }

  public String getAverage() {
    return average;
  }

  public DeviceMeasurement setAverage(String average) {
    this.average = average;
    return this;
  }

  public ReadingType getReadingType() {

    if (readingType == null) {
      readingType = new ReadingType(name);
    }

    return readingType;
  }

  public AqiIndex getAQI() {
    return AqiIndex.getAQIForLevel(average);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    DeviceMeasurementParcelablePlease.writeToParcel(this, dest, flags);
  }

  public static final Creator<DeviceMeasurement> CREATOR = new Creator<DeviceMeasurement>() {
    public DeviceMeasurement createFromParcel(Parcel source) {
      DeviceMeasurement target = new DeviceMeasurement();
      DeviceMeasurementParcelablePlease.readFromParcel(target, source);
      return target;
    }

    public DeviceMeasurement[] newArray(int size) {
      return new DeviceMeasurement[size];
    }
  };
}
