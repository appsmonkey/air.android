package io.cityos.cityosair.data.model.map;

import io.realm.RealmObject;

public class NewLocation extends RealmObject {
  private double lat;
  private double lng;

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }
}