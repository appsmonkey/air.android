package io.cityos.cityosair.data.messages.requests;

public class DeviceIdPayload {

  private String token;

  public DeviceIdPayload(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
