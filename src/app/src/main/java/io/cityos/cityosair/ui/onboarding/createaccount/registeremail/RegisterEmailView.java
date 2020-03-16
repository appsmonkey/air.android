package io.cityos.cityosair.ui.onboarding.createaccount.registeremail;

import io.cityos.cityosair.data.messages.responses.RegisterAccountResponse;
import io.cityos.cityosair.ui.map.BaseView;

public interface RegisterEmailView extends BaseView {
  void onRequestFinished(RegisterAccountResponse registerAccountEntity);
  void onRequestError(Throwable e);
}
