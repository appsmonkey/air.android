package io.cityos.cityosair.data.messages.requests;

import io.cityos.cityosair.data.model.Coordinates;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;

public class AddDevicePayload extends RealmObject implements Serializable {
  @PrimaryKey long id = 0L;
  private String token;
  private String name;
  private String model = "BOXY";
  private Coordinates coordinates;
  private boolean indoor;
  private String city;

  public AddDevicePayload() {

  }

  public String getToken() {
    return token;
  }

  public AddDevicePayload setToken(String token) {
    this.token = token;
    return this;
  }

  public AddDevicePayload setName(String name) {
    this.name = name;
    return this;
  }

  public AddDevicePayload setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public AddDevicePayload setIndoor(boolean indoor) {
    this.indoor = indoor;
    return this;
  }

  public String getName() {
    return name;
  }

  public String getModel() {
    return model;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public boolean isIndoor() {
    return indoor;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
