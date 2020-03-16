package io.cityos.cityosair.data.model.entities;

public class ValidateEmailEntity {
  private boolean exists;
  private boolean confirmed;

  public boolean isExists() {
    return exists;
  }

  public ValidateEmailEntity setExists(boolean exists) {
    this.exists = exists;
    return this;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public ValidateEmailEntity setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
    return this;
  }
}
