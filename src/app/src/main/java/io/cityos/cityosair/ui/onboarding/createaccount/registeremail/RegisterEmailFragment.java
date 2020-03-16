package io.cityos.cityosair.ui.onboarding.createaccount.registeremail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.data.messages.responses.RegisterAccountResponse;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.onboarding.createaccount.verifyemail.VerifyEmailFragment;
import io.cityos.cityosair.util.app.AndroidUtils;

public class RegisterEmailFragment extends NewBaseFragment implements RegisterEmailView {

  @Inject
  RegisterEmailPresenter registerEmailPresenter;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.email) EditText email;

  @BindString(R.string.error_enter_email) String strErrorEnterEmail;
  @BindString(R.string.error_valid_email) String strErrorValidEmail;
  @BindString(R.string.checking_email) String strCheckingEmail;
  @BindString(R.string.email_exists) String strEmailExists;
  @BindString(R.string.create_account) String strCreateAccount;

  @OnClick(R.id.btn_create_account)
  void onCreateAccountClicked() {
    setLoadingView(strCheckingEmail);
    // if email is valid and not already registered on backend start registration process
    if (inputValid(email.getText().toString())) {
      registerEmailPresenter.registerUser(email.getText().toString());
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);

    registerEmailPresenter.setView(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strCreateAccount, true);

    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return registerEmailPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_create_account;
  }

  @Override
  public void onRequestFinished(RegisterAccountResponse response) {
    setContentView();

    // if request successfully finished show verify email info fragment
    FragmentManager manager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    Bundle bundle = new Bundle();
    bundle.putString(VerifyEmailFragment.EMAIL, email.getText().toString());
    VerifyEmailFragment verifyEmailFragment = new VerifyEmailFragment();
    verifyEmailFragment.setArguments(bundle);

    transaction.replace(R.id.a_layout, verifyEmailFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onRequestError(Throwable e) {
    setContentView();

    showDialog(strEmailExists);
  }

  private boolean inputValid(String email) {
    boolean isValid = true;
    String message = "";
    if (email.isEmpty()) {
      message = "You must fill this field";
      this.email.setError("You must fill this field");
      isValid = false;
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      message = "Invalid email format";
      this.email.setError("Invalid email format");
      isValid = false;
    }

    if (!isValid) {
      showDialog(message);
    }

    return isValid;
  }
}
