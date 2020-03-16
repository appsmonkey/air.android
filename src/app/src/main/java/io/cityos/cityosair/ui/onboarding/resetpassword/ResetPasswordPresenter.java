package io.cityos.cityosair.ui.onboarding.resetpassword;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.data.datasource.datafetch.ForgotPasswordStartDataFetch;
import io.cityos.cityosair.data.datasource.exceptions.RetrofitException;
import io.cityos.cityosair.data.messages.requests.ValidateEmailPayload;
import io.cityos.cityosair.data.messages.responses.ApiErrorResponseContainer;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class ResetPasswordPresenter extends BasePresenter<ResetPasswordView> {

  private ForgotPasswordStartDataFetch forgotPasswordStartDataFetch;

  @Inject ResetPasswordPresenter(ForgotPasswordStartDataFetch forgotPasswordStartDataFetch) {
    this.forgotPasswordStartDataFetch = forgotPasswordStartDataFetch;
  }

  void startForgotPassword(String email) {
    getCompositeDisposable().add(
        forgotPasswordStartDataFetch.fetch(new ValidateEmailPayload(email)).subscribeWith(new DisposableObserver<Object>() {
          @Override
          public void onNext(Object o) {
            getView().resetPasswordStarted();
          }

          @Override
          public void onError(Throwable e) {
            if (e instanceof RetrofitException) {
              int code = ((RetrofitException) e).getCode();
              if (code == 403) {
                Response response = ((RetrofitException) e).getResponse();
                JSONObject jsonObjectError;
                try {
                  jsonObjectError = new JSONObject(response.errorBody().string());
                  Gson gson = new Gson();
                  ApiErrorResponseContainer error = gson.fromJson(jsonObjectError.toString(), ApiErrorResponseContainer.class);
                  if (error.getErrors().size() > 0) {
                    getView().resetPasswordFailed(error.getErrors().get(0).getErrorMessage());
                  } else {
                    getView().resetPasswordFailed();
                  }
                } catch (JSONException | IOException ex) {
                  ex.printStackTrace();
                }
              } else {
                getView().resetPasswordFailed();
              }
            } else {
              getView().resetPasswordFailed();
            }
          }

          @Override
          public void onComplete() {

          }
        }));
  }
}
