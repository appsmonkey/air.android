package io.cityos.cityosair.data.datasource.datafetch;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.reactivex.Observable;

public class VerifyEmailDataFetch extends AbstractDataFetch<VerifyEmailResponse, VerifyEmailPayload> {

  @Inject
  VerifyEmailDataFetch() {}

  @Override
  protected Observable<VerifyEmailResponse> fetchData(VerifyEmailPayload payload) {
    return api.getClient().verifyEmail(payload.getClientId(), payload.getUserName(),
                                       payload.getConfirmationCode(), payload.getType(), payload.getCognitoId())
        .map(Base::getData);
  }
}
