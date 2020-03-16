package io.cityos.cityosair.data.messages.requests;

import com.google.gson.annotations.SerializedName;

public class UpdateProfilePayload {
  @SerializedName("first_name") private String firstName;
  @SerializedName("last_name") private String lastName;
  @SerializedName("birthday") private long birthday;

  public UpdateProfilePayload(String firstName, String lastName, long birthday) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
  }

  public String getFirstName() {
    return firstName;
  }

  public UpdateProfilePayload setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public UpdateProfilePayload setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public long getBirthday() {
    return birthday;
  }

  public UpdateProfilePayload setBirthday(long birthday) {
    this.birthday = birthday;
    return this;
  }
}
