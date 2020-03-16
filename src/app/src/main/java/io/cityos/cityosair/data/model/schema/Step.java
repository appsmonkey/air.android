package io.cityos.cityosair.data.model.schema;

import io.realm.RealmObject;

public class Step extends RealmObject {

  private double from;
  private double to;
  private String result;

  public Step() {
  }

  public double getFrom() {
    return from;
  }

  public double getTo() {
    return to;
  }

  public String getResult() {
    return result;
  }
}
