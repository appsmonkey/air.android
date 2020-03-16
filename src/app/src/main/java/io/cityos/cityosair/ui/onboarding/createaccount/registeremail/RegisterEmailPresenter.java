package io.cityos.cityosair.ui.onboarding.createaccount.registeremail;

import android.util.Log;

import javax.inject.Inject;

import io.cityos.cityosair.data.datasource.datafetch.RegisterEmailDataFetch;
import io.cityos.cityosair.data.messages.requests.RegisterAccountRequest;
import io.cityos.cityosair.data.messages.responses.RegisterAccountResponse;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;

public class RegisterEmailPresenter extends BasePresenter<RegisterEmailView> {
  private String TAG = RegisterEmailPresenter.class.getSimpleName();

  private RegisterEmailDataFetch registerEmailDataFetch;

  @Inject
  RegisterEmailPresenter(RegisterEmailDataFetch registerEmailDataFetch) {
    this.registerEmailDataFetch = registerEmailDataFetch;
  }

  void registerUser(String email) {
    getCompositeDisposable().add(registerEmailDataFetch.fetch(new RegisterAccountRequest(email))
        .subscribeWith(new DisposableObserver<RegisterAccountResponse>() {
          @Override
          public void onNext(RegisterAccountResponse registerAccountEntity) {
            getView().onRequestFinished(registerAccountEntity);
          }
          @Override
          public void onError(Throwable e) {
            Log.d(TAG, e.toString());
            getView().onRequestError(e);
          }
          @Override
          public void onComplete() {
            Log.d(TAG, "onComplete");
          }
        }));
  }
}
