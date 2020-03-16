package io.cityos.cityosair.data.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyEmailResponse {
  @SerializedName("cognito_id")
  @Expose
  String cognitoId;
  @SerializedName("group_id")
  @Expose
  String groupId;
  @SerializedName("is_group")
  @Expose
  Boolean isGroup;
  @SerializedName("token")
  @Expose
  String token;

  public String getCognitoId() {
    return cognitoId;
  }

  public void setCognitoId(String cognitoId) {
    this.cognitoId = cognitoId;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public Boolean getGroup() {
    return isGroup;
  }

  public void setGroup(Boolean group) {
    isGroup = group;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
