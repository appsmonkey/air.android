package io.cityos.cityosair.data.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiErrorResponse {
  @SerializedName("error-code")
  @Expose
  private Integer errorCode;
  @SerializedName("error-message")
  @Expose
  private String errorMessage;
  @SerializedName("error-data")
  @Expose
  private String errorData;

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorData() {
    return errorData;
  }

  public void setErrorData(String errorData) {
    this.errorData = errorData;
  }

}