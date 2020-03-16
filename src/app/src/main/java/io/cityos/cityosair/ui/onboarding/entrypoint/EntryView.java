package io.cityos.cityosair.ui.onboarding.entrypoint;

import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.map.BaseView;

public interface EntryView extends BaseView {

  void setLoadingView();

  void setContentView();

  void loginSuccessful(User user);

}
