package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.messages.requests.ValidateEmailPayload;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class ForgotPasswordStartDataFetch
    extends AbstractDataFetch<Object, ValidateEmailPayload> {

  @Inject ForgotPasswordStartDataFetch() {
  }

  @Override protected Observable<Object> fetchData(ValidateEmailPayload payload) {
    return api.getClient().startForgotPassword(payload);
  }
}
