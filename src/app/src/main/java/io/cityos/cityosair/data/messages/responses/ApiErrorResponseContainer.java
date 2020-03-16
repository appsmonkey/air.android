package io.cityos.cityosair.data.messages.responses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiErrorResponseContainer {

  @SerializedName("code")
  @Expose
  private Integer code;
  @SerializedName("errors")
  @Expose
  private List<ApiErrorResponse> errors = null;
  @SerializedName("data")
  @Expose
  private Boolean data;
  @SerializedName("user_groups")
  @Expose
  private Object userGroups;
  @SerializedName("request_id")
  @Expose
  private String requestId;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public List<ApiErrorResponse> getErrors() {
    return errors;
  }

  public void setErrors(List<ApiErrorResponse> errors) {
    this.errors = errors;
  }

  public Boolean getData() {
    return data;
  }

  public void setData(Boolean data) {
    this.data = data;
  }

  public Object getUserGroups() {
    return userGroups;
  }

  public void setUserGroups(Object userGroups) {
    this.userGroups = userGroups;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

}