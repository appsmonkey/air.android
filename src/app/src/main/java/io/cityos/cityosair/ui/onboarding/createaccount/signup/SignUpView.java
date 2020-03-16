package io.cityos.cityosair.ui.onboarding.createaccount.signup;

import io.cityos.cityosair.data.messages.responses.SignUpResponse;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.map.BaseView;

public interface SignUpView extends BaseView {

  void registrationSuccessful(SignUpResponse signUpResponse);
  void registrationUnSuccessful(String error);
  void verifyEmailSuccessful(VerifyEmailResponse verifyEmailResponse);
  void verifyEmailUnSuccessful(String error);
}
