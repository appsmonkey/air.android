package io.cityos.cityosair.ui.onboarding.loginfragment;

import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.map.BaseView;

public interface LoginView extends BaseView {

  void loginSuccessful(User user);

  void wrongEmail();

  void wrongPass();

  void loginError(String message);

  void loginServerProblem();

  void setLoadingView();

  void setContentView();

  void showForgotPasswordView();
}
