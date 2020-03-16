package io.cityos.cityosair.ui.onboarding.changepassword;

import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.ui.map.BaseView;

public interface ChangePasswordView extends BaseView {
  void passwordResetSuccessful();
  void passwordResetFailed();
  void verifyEmailSuccessful(VerifyEmailResponse verifyEmailResponse);
  void verifyEmailUnSuccessful(String error);
}
