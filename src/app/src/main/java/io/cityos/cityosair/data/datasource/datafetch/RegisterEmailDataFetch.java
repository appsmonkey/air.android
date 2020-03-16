package io.cityos.cityosair.data.datasource.datafetch;

import android.util.Log;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.messages.requests.RegisterAccountRequest;
import io.cityos.cityosair.data.messages.responses.RegisterAccountResponse;
import io.reactivex.Observable;

public class RegisterEmailDataFetch extends AbstractDataFetch<RegisterAccountResponse, RegisterAccountRequest> {
  private static String TAG = RegisterEmailDataFetch.class.getSimpleName();

  private HttpLoggingInterceptor httpLoggingInterceptor;

  @Inject
  RegisterEmailDataFetch(HttpLoggingInterceptor httpLoggingInterceptor) {
    this.httpLoggingInterceptor = httpLoggingInterceptor;
  }

  @Override
  protected Observable<RegisterAccountResponse> fetchData(RegisterAccountRequest request) {
    return api.getClient().registerAccount(request)
        .map(Base::getData)
        .doOnNext(response -> {
          Log.d(TAG, response.toString());
          httpLoggingInterceptor.setTokens(response.getIdToken(), response.getAccessToken(), "");
        });
  }
}
