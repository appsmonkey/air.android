package io.cityos.cityosair.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
  @PrimaryKey
  private int id = 0;
  private String id_token;
  private String access_token;
  private String refresh_token;
  private String email;
  private String password;
  private boolean isGuest;

  public User() {
  }

  public boolean isGuest() {
    return isGuest;
  }

  public User setGuest(boolean guest) {
    isGuest = guest;
    return this;
  }

  public String getId_token() {
    return id_token;
  }

  public User setId_token(String id_token) {
    this.id_token = id_token;
    return this;
  }

  public String getAccess_token() {
    return access_token;
  }

  public User setAccess_token(String access_token) {
    this.access_token = access_token;
    return this;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public User setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void clear() {
    this.id_token = "";
    this.access_token = "";
    this.refresh_token = "";
    this.email = "";
    this.password = "";
    this.isGuest = false;
  }
}
