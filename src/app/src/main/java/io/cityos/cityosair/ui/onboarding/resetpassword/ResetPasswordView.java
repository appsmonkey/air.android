package io.cityos.cityosair.ui.onboarding.resetpassword;

import io.cityos.cityosair.ui.map.BaseView;

public interface ResetPasswordView extends BaseView {
  void emailValidation(boolean exists);
  void resetPasswordStarted();
  void resetPasswordFailed();
  void resetPasswordFailed(String message);
}
