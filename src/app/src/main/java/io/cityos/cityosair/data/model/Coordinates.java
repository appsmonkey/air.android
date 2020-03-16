package io.cityos.cityosair.data.model;

import io.realm.RealmObject;
import java.io.Serializable;

public class Coordinates extends RealmObject implements Serializable {
  private double lat;
  private double lng;

  public Coordinates() {
  }

  public Coordinates(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }
}
