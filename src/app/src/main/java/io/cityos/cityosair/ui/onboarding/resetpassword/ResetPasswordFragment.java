package io.cityos.cityosair.ui.onboarding.resetpassword;

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
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import javax.inject.Inject;

import static android.app.PendingIntent.getActivity;

public class ResetPasswordFragment extends NewBaseFragment implements ResetPasswordView {

  @Inject
  ResetPasswordPresenter resetPasswordPresenter;
  @Inject
  SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.email)
  EditText email;

  @BindString(R.string.reset_password)
  String strResetPassword;

  @OnClick(R.id.btn_continue)
  void onContinueClicked() {
    setLoadingView(null);
    // if email is valid start reset password process on api
    if (validateEmail(email.getText().toString())) {
      resetPasswordPresenter.startForgotPassword(email.getText().toString());
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
    resetPasswordPresenter.setView(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strResetPassword, true);

    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return resetPasswordPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_reset_password;
  }

  @Override
  public void emailValidation(boolean exists) {
    if (!exists) {
      setContentView();
      showDialog(getString(R.string.email_not_registered));
    } else {
      resetPasswordPresenter.startForgotPassword(email.getText().toString());
    }
  }

  @Override
  public void resetPasswordStarted() {
    setContentView();
    // if reset password was sucessfully started show dialog that verification link is sent to email
    showDialog("Verification link has been sent to your email");
  }

  @Override
  public void resetPasswordFailed() {
    setContentView();
    showDialog(getString(R.string.email_not_registered));
  }

  @Override
  public void resetPasswordFailed(String message) {
    setContentView();
    showDialog(message);
  }
}
