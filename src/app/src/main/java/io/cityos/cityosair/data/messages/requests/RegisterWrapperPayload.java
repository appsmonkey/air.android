package io.cityos.cityosair.data.messages.requests;

public class RegisterWrapperPayload {
  private RegisterPayload registerPayload;
  private UpdateProfilePayload updateProfilePayload;

  public RegisterWrapperPayload(RegisterPayload registerPayload,
      UpdateProfilePayload updateProfilePayload) {
    this.registerPayload = registerPayload;
    this.updateProfilePayload = updateProfilePayload;
  }

  public RegisterPayload getRegisterPayload() {
    return registerPayload;
  }

  public RegisterWrapperPayload setRegisterPayload(RegisterPayload registerPayload) {
    this.registerPayload = registerPayload;
    return this;
  }

  public UpdateProfilePayload getUpdateProfilePayload() {
    return updateProfilePayload;
  }

  public RegisterWrapperPayload setUpdateProfilePayload(UpdateProfilePayload updateProfilePayload) {
    this.updateProfilePayload = updateProfilePayload;
    return this;
  }
}
