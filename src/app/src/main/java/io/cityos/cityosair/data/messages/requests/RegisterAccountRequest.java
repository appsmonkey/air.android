package io.cityos.cityosair.data.messages.requests;

import java.io.Serializable;

public class RegisterAccountRequest implements Serializable {
  private String email;


  public RegisterAccountRequest(String email) {
    this.email = email;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
