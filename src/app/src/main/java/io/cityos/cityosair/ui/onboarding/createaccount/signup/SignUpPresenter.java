package io.cityos.cityosair.ui.onboarding.createaccount.signup;

import javax.inject.Inject;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.datasource.datafetch.RegisterDataFetch;
import io.cityos.cityosair.data.datasource.datafetch.VerifyEmailDataFetch;
import io.cityos.cityosair.data.messages.requests.SignUpPayload;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.SignUpResponse;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

class SignUpPresenter extends BasePresenter<SignUpView> {

  private RegisterDataFetch registerDataFetch;
  private VerifyEmailDataFetch verifyEmailDataFetch;

  @Inject
  SignUpPresenter(RegisterDataFetch registerDataFetch, VerifyEmailDataFetch verifyEmailDataFetch) {
    this.registerDataFetch = registerDataFetch;
    this.verifyEmailDataFetch = verifyEmailDataFetch;
  }

  void registerUser(SignUpPayload payload) {
    getCompositeDisposable().add(registerDataFetch.fetch(payload)
        .subscribeWith(new DisposableObserver<SignUpResponse>() {
          @Override public void onNext(SignUpResponse signUpResponse) {
            getView().registrationSuccessful(signUpResponse);
          }
          @Override public void onError(Throwable e) {
            getView().registrationUnSuccessful(e.toString());
          }
          @Override public void onComplete() { }
        }));
  }

  void verifyEmail(VerifyEmailPayload payload) {
    getCompositeDisposable().add(verifyEmailDataFetch.fetch(payload)
        .subscribeWith(new DisposableObserver<VerifyEmailResponse>() {
          @Override public void onNext(VerifyEmailResponse verifyEmailResponse) {
            getView().verifyEmailSuccessful(verifyEmailResponse);
          }
          @Override public void onError(Throwable e) {
            getView().verifyEmailUnSuccessful(e.toString());
          }
          @Override public void onComplete() { }
        }));
  }
}
