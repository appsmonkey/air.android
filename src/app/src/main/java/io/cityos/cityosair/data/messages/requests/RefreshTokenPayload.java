package io.cityos.cityosair.data.messages.requests;

public class RefreshTokenPayload {

  private String refresh_token;

  public RefreshTokenPayload(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getToken() {
    return refresh_token;
  }
}
