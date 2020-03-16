package io.cityos.cityosair.data.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpPayload {
  @SerializedName("user_profile")
  @Expose
  UserProfile userProfile;
  @SerializedName("token")
  @Expose
  String token;
  @SerializedName("cognito_id")
  @Expose
  String cogitoId;
  @SerializedName("user_name")
  @Expose
  String userName;
  @SerializedName("password")
  @Expose
  String password;

  public SignUpPayload(UserProfile userProfile, String token, String cogitoId, String userName, String password) {
    this.userProfile = userProfile;
    this.token = token;
    this.cogitoId = cogitoId;
    this.userName = userName;
    this.password = password;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getCogitoId() {
    return cogitoId;
  }

  public void setCogitoId(String cogitoId) {
    this.cogitoId = cogitoId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static class UserProfile {
    @SerializedName("first_name")
    @Expose
    String firstName;
    @SerializedName("last_name")
    @Expose
    String lastName;
    @SerializedName("bio")
    @Expose
    String bio;
    @SerializedName("City")
    @Expose
    String city;
    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("birthday")
    @Expose
    long birthday;

    public UserProfile(String firstName, String lastName, String bio, String city, String gender, long birthday) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.bio = bio;
      this.city = city;
      this.gender = gender;
      this.birthday = birthday;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getBio() {
      return bio;
    }

    public void setBio(String bio) {
      this.bio = bio;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public long getBirthday() {
      return birthday;
    }

    public void setBirthday(long birthday) {
      this.birthday = birthday;
    }
  }
}
