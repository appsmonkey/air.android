package io.cityos.cityosair.data.messages.requests;

import io.cityos.cityosair.data.model.Social;

public class SocialLoginPayload {
  private String email;
  private Social social;

  public SocialLoginPayload(String email, Social social) {
    this.email = email;
    this.social = social;
  }

  public SocialLoginPayload setEmail(String email) {
    this.email = email;
    return this;
  }

  public Social getSocial() {
    return social;
  }

  public SocialLoginPayload setSocial(Social social) {
    this.social = social;
    return this;
  }

  public String getEmail() {
    return email;
  }
}
