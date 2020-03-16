package io.cityos.cityosair.data.datasource.datafetch;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.data.messages.requests.SignUpPayload;
import io.cityos.cityosair.data.messages.responses.SignUpResponse;
import io.reactivex.Observable;

public class RegisterDataFetch extends AbstractDataFetch<SignUpResponse, SignUpPayload> {

  @Inject
  RegisterDataFetch() { }

  @Override
  protected Observable<SignUpResponse> fetchData(SignUpPayload signUpPayload) {
    return api.getClient().signUp(signUpPayload);
  }
}
