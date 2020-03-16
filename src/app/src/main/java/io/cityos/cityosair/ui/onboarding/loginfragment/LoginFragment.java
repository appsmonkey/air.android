package io.cityos.cityosair.ui.onboarding.loginfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.urbanairship.UAirship;
import io.cityos.cityosair.BuildConfig;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.AuthCredentials;
import io.cityos.cityosair.data.model.Social;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.main.MainActivity;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.onboarding.resetpassword.ResetPasswordFragment;
import io.cityos.cityosair.ui.onboarding.createaccount.registeremail.RegisterEmailFragment;
import io.cityos.cityosair.ui.notifications.NotificationsEnum;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import java.util.Set;
import javax.inject.Inject;

public class LoginFragment extends NewBaseFragment implements LoginView {

  @Inject LoginPresenter loginPresenter;
  @Inject SharedPreferencesManager sharedPreferencesManager;

  private String email;
  private String password;
  private CallbackManager callbackManager;
  private GoogleSignInClient mGoogleSignInClient;

  @BindView(R.id.parent_layout) LinearLayout parentLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progress_overlay) View progressOverlay;
  @BindView(R.id.et_email) EditText inputEmail;
  @BindView(R.id.et_password) EditText inputPassword;
  @BindView(R.id.facebook_button) Button facebookButton;
  @BindView(R.id.google_button) Button googleButton;

  @BindString(R.string.settings_login) String strLoginDevice;
  @BindString(R.string.login_loading_text) String strLoginLoadingText;
  @BindString(R.string.login_loading_text) String strErrorWrongEmail;
  @BindString(R.string.error_wrong_pass) String strErrorWrongPass;
  @BindString(R.string.error_server_problems) String strErrorServerProblems;
  @BindString(R.string.error_enter_email) String strErrorEnterEmail;
  @BindString(R.string.error_valid_email) String strErrorValidEmail;
  @BindString(R.string.error_enter_password) String strErrorEnterPassword;

  @OnClick(R.id.btn_noDevice)
  void noDeviceClicked() {
    loginPresenter.getGuestDevicesAndSchema();
  }

  @OnClick(R.id.btn_forgotPass)
  void onForgotPassClicked() {
    FragmentManager manager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    transaction.replace(R.id.a_layout, new ResetPasswordFragment());
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @OnClick(R.id.google_button)
  void onGoogleButtonClicked() {
    loginPresenter.loginWithGoogle(mGoogleSignInClient, this);
  }

  @OnClick(R.id.facebook_button)
  void onFacebookButtonClicked() {
    loginPresenter.loginWithFacebook(this, callbackManager);
  }

  @OnClick(R.id.btn_sign_up_email)
  void signUpClicked() {
    FragmentManager manager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    transaction.replace(R.id.a_layout, new RegisterEmailFragment());
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @OnClick(R.id.btn_login)
  void loginClicked() {

    setLoadingView();

    email = inputEmail.getText().toString().trim();
    password = inputPassword.getText().toString().trim();

    if (inputValid(email, password)) {
      hideKeyboard();
      loginPresenter.login(new AuthCredentials(email, password));
    } else {
      setContentView();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);

    loginPresenter.setView(this);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_API_ID)
            .requestEmail()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

    callbackManager = CallbackManager.Factory.create();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strLoginDevice, true);

    Drawable facebookImage = AppCompatResources.getDrawable(getContext(), R.drawable.ic_facebook);
    facebookImage.setBounds(0, 0, 60, 60);
    facebookButton.setCompoundDrawables(facebookImage, null, null, null);

    Drawable googleImage = AppCompatResources.getDrawable(getContext(), R.drawable.ic_google);
    googleImage.setBounds(0, 0, 60, 60);
    googleButton.setCompoundDrawables(googleImage, null, null, null);

    autoLoginIfDeepLink();
  }

  private void autoLoginIfDeepLink() {
    LoginActivity loginActivity = (LoginActivity)getActivity();
    if (loginActivity.getIsDeeplink()) {
      loginActivity.setIsDeeplink(false);
      setLoadingView();
      loginPresenter.login(new AuthCredentials(loginActivity.getEmail(), loginActivity.getPassword()));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    if (item.getItemId() == android.R.id.home) {
      if (getActivity() != null) {
        getActivity().onBackPressed();
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void navigateToMainActivity() {
    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    getActivity().finish();
  }

  @Override
  public void loginSuccessful(User user) {
    Set tags;
    tags = UAirship.shared().getPushManager().getTags();
    tags.clear();
    tags.add(NotificationsEnum.LAST_GOOD.getTag());
    tags.add(NotificationsEnum.SETTING_SENSITIVE.getTag());

    // this is a guest user
    if (user == null) {
      tags.add(SharedPreferencesManager.REGISTERED_FALSE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED,
              SharedPreferencesManager.REGISTERED_FALSE);
    } else {
      tags.add(SharedPreferencesManager.REGISTERED_TRUE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED,
              SharedPreferencesManager.REGISTERED_TRUE);
    }

    sharedPreferencesManager.set(SharedPreferencesManager.LAST_NOTIFICATION,
            NotificationsEnum.LAST_GOOD.getTag());
    sharedPreferencesManager.set(SharedPreferencesManager.SETTINGS_NOTIFICATION,
            NotificationsEnum.SETTING_SENSITIVE.getTag());

    UAirship.shared().getPushManager().setTags(tags);
    UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    UAirship.shared().getPushManager().setPushEnabled(true);

    navigateToMainActivity();
  }

  @Override
  public void wrongEmail() {
    loadInputET();
    Snackbar.make(parentLayout, strErrorWrongEmail, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void wrongPass() {
    new AlertDialog.Builder(getContext())
            .setMessage(R.string.login_error_check_password_social)
            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
            })
            .show();

    loadInputET();
    Snackbar.make(parentLayout, strErrorWrongPass, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void loginServerProblem() {
    loadInputET();
    Snackbar.make(parentLayout, strErrorServerProblems, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void loginError(String message) {
    setContentView();
    new AlertDialog.Builder(getContext())
        .setMessage(message)
        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
        })
        .show();

    loadInputET();
    Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
  }

  @Override protected BasePresenter getFragmentPresenter() {
    return loginPresenter;
  }

  @Override protected int getFragmentLayoutId() {
    return R.layout.fragment_login;
  }

  @Override
  public void setLoadingView() {
    AndroidUtils.showLoadingView(progressOverlay, strLoginLoadingText);
  }

  @Override
  public void setContentView() {
    AndroidUtils.hideLoadingView(progressOverlay);
  }

  @Override
  public void showForgotPasswordView() {
    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cityos.io/reset.account"));
    startActivity(launchBrowser);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);

    if (requestCode == Constants.GOOGLE_RESULT_CODE) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleSignInResult(task);
    }
  }

  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      //updateUI(account);
      loginPresenter.socialLogin(account.getEmail(), account.getId(), account.getIdToken(), Social.TYPE.G);
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.e("LOGLOG", "signInResult:failed code=" + e.getStatusCode());
      //updateUI(null);
    }
  }

  /**
   * Validates iput of email and password, returns true if everything
   * valid, false otherwise.
   *
   * @param email email as entered by the user.
   * @param password password as entered by the user.
   * @return true if valid input, false otherwise.
   */
  private boolean inputValid(String email, String password) {
    boolean isValid = true;

    //Email field checks
    if (email == null || email.isEmpty()) {
      inputEmail.setError(strErrorEnterEmail);
      isValid = false;
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      inputEmail.setError(strErrorValidEmail);
      isValid = false;
    }

    // Passsword field check.
    if (password == null || password.isEmpty()) {
      inputPassword.setError(strErrorEnterPassword);
      isValid = false;
    }

    return isValid;
  }

  private void hideKeyboard() {
    View view = this.getActivity().getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  //zarko.runjevac 5.2.2018 on some devices edittextview loses text
  //and this utility load edittextview
  private void loadInputET() {
    if (inputEmail.getText().toString().trim().isEmpty()) {
      if (email == null || !email.isEmpty()) {
        inputEmail.setText(email);
      }
    }
    if (inputPassword.getText().toString().trim().isEmpty()) {
      if (password == null || !password.isEmpty()) {
        inputPassword.setText(password);
      }
    }
  }


}
