package io.cityos.cityosair.data.messages.requests;

public class RegisterPayload {
  private String email;
  private String password;

  public RegisterPayload(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public RegisterPayload setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public RegisterPayload setPassword(String password) {
    this.password = password;
    return this;
  }
}
