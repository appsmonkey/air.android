package io.cityos.cityosair.data.datasource.datafetch;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.data.model.ForgotPasswordEndPayload;
import io.reactivex.Observable;

public class ForgotPasswordEndDataFetch
    extends AbstractDataFetch<Object, ForgotPasswordEndPayload> {

  @Inject ForgotPasswordEndDataFetch() {
  }

  @Override
  protected Observable<Object> fetchData(ForgotPasswordEndPayload payload) {
    return api.getClient().endForgotPassword(payload);
  }

}
