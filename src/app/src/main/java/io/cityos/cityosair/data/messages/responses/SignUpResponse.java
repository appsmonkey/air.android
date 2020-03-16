package io.cityos.cityosair.data.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse {
  @SerializedName("request_id")
  @Expose
  String requestId;
  @SerializedName("code")
  @Expose
  Integer code;

  public SignUpResponse(String requestId, Integer code) {
    this.requestId = requestId;
    this.code = code;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
