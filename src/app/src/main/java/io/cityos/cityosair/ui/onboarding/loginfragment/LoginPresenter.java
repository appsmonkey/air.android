package io.cityos.cityosair.ui.onboarding.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.datasource.datafetch.GuestSchemaDataFetch;
import io.cityos.cityosair.data.datasource.datafetch.LoginDataFetch;
import io.cityos.cityosair.data.datasource.datafetch.SocialLoginDataFetch;
import io.cityos.cityosair.data.datasource.exceptions.RetrofitException;
import io.cityos.cityosair.data.messages.requests.SocialLoginPayload;
import io.cityos.cityosair.data.messages.responses.ApiErrorResponseContainer;
import io.cityos.cityosair.data.model.AuthCredentials;
import io.cityos.cityosair.data.model.Social;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<LoginView> {
  private String TAG = LoginPresenter.class.getSimpleName();

  private LoginDataFetch loginDataFetch;
  private SocialLoginDataFetch socialLoginDataFetch;
  private HttpLoggingInterceptor httpLoggingInterceptor;
  private GuestSchemaDataFetch guestSchemaDataFetch;

  private static final int ERROR_CODE_WRONG_CREDENTIALS = 403;
  private static final int ERROR_CODE_UNAUTHORIZED = 401;
  private static final int ERROR_CODE_NOT_FOUND = 404;

  @Inject LoginPresenter(LoginDataFetch loginDataFetch,
      HttpLoggingInterceptor httpLoggingInterceptor,
      GuestSchemaDataFetch guestSchemaDataFetch,
      SocialLoginDataFetch socialLoginDataFetch) {
    this.loginDataFetch = loginDataFetch;
    this.socialLoginDataFetch = socialLoginDataFetch;
    this.guestSchemaDataFetch = guestSchemaDataFetch;
    this.httpLoggingInterceptor = httpLoggingInterceptor;
  }

  void getGuestDevicesAndSchema() {
    getView().setLoadingView();

    getCompositeDisposable().add(
        guestSchemaDataFetch.fetch(null).subscribeWith(new DisposableObserver<Object>() {

          @Override public void onNext(Object o) {
            getView().setContentView();
            getView().loginSuccessful(null);
          }

          @Override public void onError(Throwable e) {
            getView().setContentView();
            genericError();
          }

          @Override public void onComplete() {
          }
        })
    );
  }

  void socialLogin(String email, String id, String tokenId, Social.TYPE type) {
    getCompositeDisposable().add(socialLoginDataFetch
            .fetch(new SocialLoginPayload(email, new Social(id, tokenId, type)))
            .subscribeWith(new LoginDisposableObserver())
    );
  }

  void loginWithFacebook(Fragment fragment, CallbackManager callbackManager) {
    getView().setLoadingView();
    LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
          @Override public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                  try {
                    String email = object.getString("email");
                    String userId = loginResult.getAccessToken().getUserId();
                    String token = loginResult.getAccessToken().getToken();

                    LoginPresenter.this.socialLogin(email, userId, token, Social.TYPE.FB);
                  } catch (JSONException e) {
                    e.printStackTrace();
                    getView().setContentView();
                    genericError();
                  }
                });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
          }

          @Override public void onCancel() {
            getView().setContentView();
          }

          @Override public void onError(FacebookException error) {
            getView().setContentView();
            genericError();
          }
        });

    LoginManager.getInstance()
        .logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
  }

  void loginWithGoogle(GoogleSignInClient mGoogleSignInClient, Fragment fragment) {
    //getView().setLoadingView();
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    fragment.startActivityForResult(signInIntent, Constants.GOOGLE_RESULT_CODE);
  }

  void login(AuthCredentials authCredentials) {
    getView().setLoadingView();
    getCompositeDisposable().add(loginDataFetch.fetch(authCredentials).subscribeWith(new LoginDisposableObserver()));
  }

  private class LoginDisposableObserver extends DisposableObserver<User> {
    @Override public void onNext(User user) {
      Log.d(TAG, "ON_NEXT_USER: " + user);
      getView().setContentView();
      getView().loginSuccessful(user);
      httpLoggingInterceptor.setTokens(user.getId_token(), user.getAccess_token(), user.getRefresh_token());
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
      if (e instanceof RetrofitException) {
        if (((RetrofitException) e).getCode() == ERROR_CODE_WRONG_CREDENTIALS) {
          Response response = ((RetrofitException) e).getResponse();
          JSONObject jsonObjectError = null;
          try {
            jsonObjectError = new JSONObject(response.errorBody().string());
          } catch (IOException | JSONException ex) {
            ex.printStackTrace();
          }
          try {
            String errorMessage = jsonObjectError.getJSONArray("errors").getJSONObject(0).getString("error-message");
            if (errorMessage.contains("social")) {
              Gson gson = new Gson();
              ApiErrorResponseContainer error = gson.fromJson(jsonObjectError.toString(), ApiErrorResponseContainer.class);
              if (error.getErrors().size() > 0) {
                getView().loginError(error.getErrors().get(0).getErrorMessage());
              } else {
                wrongPass();
              }
            } else {
              wrongPass();
            }
          } catch (JSONException ex) {
            ex.printStackTrace();
            wrongPass();
          }
        } else if (((RetrofitException) e).getCode() == ERROR_CODE_UNAUTHORIZED) {
          wrongPass();
        } else if (((RetrofitException) e).getCode() == ERROR_CODE_NOT_FOUND) {
          wrongEmail();
        } else {
          genericError();
        }
      }
    }

    @Override public void onComplete() {
      getView().setContentView();
    }
  }

  private void wrongEmail() {
    getView().setContentView();
    getView().wrongEmail();
  }

  private void wrongPass() {
    getView().setContentView();
    getView().wrongPass();
  }

  private void genericError() {
    getView().setContentView();
    getView().loginServerProblem();
  }

}
