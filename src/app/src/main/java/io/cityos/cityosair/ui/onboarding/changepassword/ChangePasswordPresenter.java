package io.cityos.cityosair.ui.onboarding.changepassword;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.cityos.cityosair.BuildConfig;
import io.cityos.cityosair.data.datasource.datafetch.VerifyEmailDataFetch;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.data.model.ForgotPasswordEndPayload;
import io.cityos.cityosair.data.datasource.datafetch.ForgotPasswordEndDataFetch;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import javax.inject.Inject;

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView> {
  private static String TAG = ChangePasswordPresenter.class.getSimpleName();

  private CompositeDisposable compositeDisposable;
  ForgotPasswordEndDataFetch forgotPasswordEndDataFetch;
  VerifyEmailDataFetch verifyEmailDataFetch;

  @Inject
  ChangePasswordPresenter(ForgotPasswordEndDataFetch forgotPasswordEndDataFetch,
                          VerifyEmailDataFetch verifyEmailDataFetch) {
    this.forgotPasswordEndDataFetch = forgotPasswordEndDataFetch;
    this.verifyEmailDataFetch = verifyEmailDataFetch;
    this.compositeDisposable = new CompositeDisposable();
  }

  void endForgotPassword(String password, String email, String token, String cognitoId) {
    compositeDisposable.add(forgotPasswordEndDataFetch.fetch(new ForgotPasswordEndPayload(email, password, token, cognitoId))
        .subscribeWith(new DisposableObserver<Object>() {
          @Override public void onNext(Object o) {
            getView().passwordResetSuccessful();
          }

          @Override public void onError(Throwable e) {
            if (!BuildConfig.DEBUG) {
              Crashlytics.log(Log.DEBUG, TAG, e.toString());
            }
            Log.e(TAG, e.toString());
            getView().passwordResetFailed();
          }

          @Override public void onComplete() {
          }
        }));
  }

  void verifyEmail(VerifyEmailPayload payload) {
    compositeDisposable.add(verifyEmailDataFetch.fetch(payload)
        .subscribeWith(new DisposableObserver<VerifyEmailResponse>() {
          @Override public void onNext(VerifyEmailResponse verifyEmailResponse) {
            getView().verifyEmailSuccessful(verifyEmailResponse);
          }
          @Override public void onError(Throwable e) {
            if (!BuildConfig.DEBUG) {
              Crashlytics.log(Log.DEBUG, TAG, e.toString());
            }
            Log.e(TAG, e.toString());
            getView().verifyEmailUnSuccessful(e.toString());
          }
          @Override public void onComplete() { }
        }));
  }

  @Override public void onStop() {
    compositeDisposable.clear();
  }

  @Override public void onDestroyView() {
    compositeDisposable.dispose();
  }

}
