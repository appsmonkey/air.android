package io.cityos.cityosair.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordEndPayload {
  @SerializedName("email")
  @Expose
  private String email;
  @SerializedName("password")
  @Expose
  private String password;
  @SerializedName("token")
  @Expose
  private String token;
  @SerializedName("cognito_id")
  @Expose
  private String cognitoId;

  public ForgotPasswordEndPayload(String email, String password, String token, String cognitoId) {
    this.email = email;
    this.password = password;
    this.token = token;
    this.cognitoId = cognitoId;
  }
}
