package io.cityos.cityosair.ui.onboarding.changepassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import javax.inject.Inject;

public class ChangePasswordFragment extends NewBaseFragment implements ChangePasswordView {

  @Inject
  ChangePasswordPresenter changePasswordPresenter;
  @Inject
  SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.password) EditText password;
  @BindView(R.id.repeat_password) EditText repeatPassword;

  @BindString(R.string.reset_password) String strResetPassword;

  public static String VERIFY_EMAIL_PAYLOAD = "verify_email_payload";
  VerifyEmailPayload verifyEmailPayload;
  VerifyEmailResponse verifyEmailResponse;


  @OnClick(R.id.btn_reset_password)
  void onResetPasswordClicked() {
    // validate passwords match
    if (validatePasswordMatching(password.getText().toString(), repeatPassword.getText().toString())) {
      setLoadingView(null);
      // complete password reset
      changePasswordPresenter.endForgotPassword(password.getText().toString(), verifyEmailPayload.getUserName(), verifyEmailResponse.getToken(), verifyEmailResponse.getCognitoId());
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
    changePasswordPresenter.setView(this);

    if (getArguments() != null) {
      // get payload parsed from deep link
      verifyEmailPayload = getArguments().getParcelable(VERIFY_EMAIL_PAYLOAD);
    }
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strResetPassword, true);

    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

    // on view created verify email from deep link
    changePasswordPresenter.verifyEmail(verifyEmailPayload);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return changePasswordPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_insert_code;
  }

  @Override
  public void verifyEmailSuccessful(VerifyEmailResponse verifyEmailResponse) {
    this.verifyEmailResponse = verifyEmailResponse;
  }

  @Override
  public void verifyEmailUnSuccessful(String error) {
    setContentView();
    if (error != null) {
      showDialog(error);
    } else {
      showDialog("Unknown Error");
    }
  }

  @Override
  public void passwordResetSuccessful() {
    setContentView();

    // reset password completed, clear stack and set login activity as a new task
    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    getActivity().finish();
  }

  @Override
  public void passwordResetFailed() {
    setContentView();
  }
}
