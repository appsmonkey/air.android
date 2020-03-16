package io.cityos.cityosair.data.messages.requests;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyEmailPayload implements Parcelable {

  public VerifyEmailPayload(String clientId, String userName, String confirmationCode, String type, String cognitoId) {
    this.clientId = clientId;
    this.userName = userName;
    this.confirmationCode = confirmationCode;
    this.type = type;
    this.cognitoId = cognitoId;
  }

  @SerializedName("cliend_id")
  @Expose
  private String clientId;
  @SerializedName("user_name")
  @Expose
  private String userName;
  @SerializedName("confirmation_code")
  @Expose
  private String confirmationCode;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("cog_id")
  @Expose
  private String cognitoId;

  public String getClientId() {
    return clientId;
  }

  public String getUserName() {
    return userName;
  }

  public String getConfirmationCode() {
    return confirmationCode;
  }

  public String getType() {
    return type;
  }

  public String getCognitoId() {
    return cognitoId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.clientId);
    dest.writeString(this.userName);
    dest.writeString(this.confirmationCode);
    dest.writeString(this.type);
    dest.writeString(this.cognitoId);
  }

  protected VerifyEmailPayload(Parcel in) {
    this.clientId = in.readString();
    this.userName = in.readString();
    this.confirmationCode = in.readString();
    this.type = in.readString();
    this.cognitoId = in.readString();
  }

  public static final Creator<VerifyEmailPayload> CREATOR = new Creator<VerifyEmailPayload>() {
    @Override
    public VerifyEmailPayload createFromParcel(Parcel source) {
      return new VerifyEmailPayload(source);
    }

    @Override
    public VerifyEmailPayload[] newArray(int size) {
      return new VerifyEmailPayload[size];
    }
  };
}
