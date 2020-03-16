package io.cityos.cityosair.data.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterAccountResponse {
  @SerializedName("id_token")
  @Expose
  String idToken;
  @SerializedName("access_token")
  @Expose
  String accessToken;
  @SerializedName("expires_in")
  @Expose
  Integer expiresIn;

  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }
}