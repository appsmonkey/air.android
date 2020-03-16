package io.cityos.cityosair.data.messages.requests;

public class ValidateEmailPayload {
  private String email;

  public ValidateEmailPayload(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public ValidateEmailPayload setEmail(String email) {
    this.email = email;
    return this;
  }
}
