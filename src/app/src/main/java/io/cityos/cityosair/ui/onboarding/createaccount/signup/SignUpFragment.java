package io.cityos.cityosair.ui.onboarding.createaccount.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.urbanairship.UAirship;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.data.messages.requests.SignUpPayload;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.SignUpResponse;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.notifications.NotificationsEnum;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;

public class SignUpFragment extends NewBaseFragment implements DatePickerDialog.OnDateSetListener, SignUpView {
  private static String TAG = SignUpFragment.class.getSimpleName();

  @Inject
  SignUpPresenter signUpPresenter;
  @Inject SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.password) EditText password;
  @BindView(R.id.verify_password) EditText verifyPassword;
  @BindView(R.id.first_name) EditText firstName;
  @BindView(R.id.last_name) EditText lastName;
  @BindView(R.id.birthday) TextView birthday;
  @BindView(R.id.txt_terms_and_conditions) TextView termsAndConditions;

  @BindString(R.string.date_of_birth) String strDateOfBirth;
  @BindString(R.string.setup_account) String strSetupAccount;
  @BindString(R.string.error_enter_email) String strErrorEnterEmail;
  @BindString(R.string.error_valid_email) String strErrorValidEmail;
  @BindString(R.string.error_enter_password) String strErrorEnterPassword;
  @BindString(R.string.you_must_fill_field) String strFillField;
  @BindString(R.string.you_must_fill_birthday) String strFillBirthday;
  @BindString(R.string.invalid_password) String strInvalidPasswordMessage;

  private long birthdayInSeconds;
  private VerifyEmailPayload verifyEmailPayload;
  private VerifyEmailResponse verifyEmailResponse;
  public static String VERIFY_EMAIL_PAYLOAD = "verify_email_payload";

  @OnClick(R.id.btn_create_account)
  void onCreateAccountClicked() {
    setLoadingView(null);
    // if input is valid register user
    if (inputValid(password.getText().toString(), verifyPassword.getText().toString())) {
      SignUpPayload.UserProfile userProfile = new SignUpPayload.UserProfile(firstName.getText().toString(),
          lastName.getText().toString(), "", "", "", birthdayInSeconds);
      SignUpPayload signUpPayload = new SignUpPayload(userProfile, verifyEmailResponse.getToken(), verifyEmailResponse.getCognitoId(),
          verifyEmailPayload.getUserName(), password.getText().toString());
      signUpPresenter.registerUser(signUpPayload);
    } else {
      setContentView();
    }
  }

  @OnClick(R.id.birthday)
  void onBirthdayClicked() {
    // user must be at least 14 years old
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    new DatePickerDialog(getContext(), this, year, month, day).show();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);

    signUpPresenter.setView(this);

    verifyEmailPayload = getArguments().getParcelable(VERIFY_EMAIL_PAYLOAD);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strSetupAccount, true);

    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

    termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

    setLoadingView(null);
    // on view created verify deep link email payload
    signUpPresenter.verifyEmail(verifyEmailPayload);
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return signUpPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_setup_account;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
    if (checkDate(year, month, day)) {
      birthday.setText(String.format(strDateOfBirth, getMonth(month), day, year));
      birthdayInSeconds = getBirthdayInSeconds(year, month, day);
    }
  }

  private boolean inputValid(String password, String verifyPassword) {
    boolean isValid = true;
    String message = "";

    if (firstName.getText().toString().isEmpty()) {
      message = strFillField;
      this.firstName.setError(strFillField);
      isValid = false;
    } else if (lastName.getText().toString().isEmpty()) {
      message = strFillField;
      this.lastName.setError(strFillField);
      isValid = false;
    } else if (birthday.getText().toString().isEmpty()) {
      message = strFillBirthday;
      this.birthday.setError(strFillField);
      isValid = false;
    }

    // Password field check.
    if (password.isEmpty()) {
      message = strErrorEnterPassword;
      this.password.setError(strErrorEnterPassword);
      isValid = false;
    } else if (!isValidPassword(password)) {
      this.password.setError("Invalid password format");
      message = strInvalidPasswordMessage;
      isValid = false;
    } else if (verifyPassword.isEmpty()) {
      message = strFillField;
      this.verifyPassword.setError(strFillField);
      isValid = false;
    } else if (!password.equals(verifyPassword)) {
      message = "The fields must match";
      this.password.setError("The fields must match");
      this.verifyPassword.setError("The fields must match");
      isValid = false;
    }

    if (!isValid) {
      showDialog(message);
    }

    return isValid;
  }

  public boolean isValidPassword(final String password) {
    Pattern pattern;
    Matcher matcher;

    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$";

    pattern = Pattern.compile(PASSWORD_PATTERN);
    matcher = pattern.matcher(password);

    return matcher.matches();
  }

  private boolean checkDate(int year, int month, int day) {
    boolean valid = true;
    Calendar userAge = new GregorianCalendar(year, month, day);
    Calendar minAdultAge = new GregorianCalendar();
    minAdultAge.add(Calendar.YEAR, -14);
    if (minAdultAge.before(userAge)) {
      valid = false;
      showDialog("You must be 14 to register.");
    }

    return valid;
  }

  private long getBirthdayInSeconds(int year, int month, int day) {
    Calendar age = new GregorianCalendar(year, month, day);
    return age.getTimeInMillis() / 1000L;
  }

  private String getMonth(int month) {
    return new DateFormatSymbols().getMonths()[month];
  }

  @Override
  public void verifyEmailSuccessful(VerifyEmailResponse verifyEmailResponse) {
    setContentView();
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
  public void registrationSuccessful(SignUpResponse signUpResponse) {
    setContentView();
    Set tags;
    tags = UAirship.shared().getPushManager().getTags();
    tags.clear();
    tags.add(NotificationsEnum.LAST_GOOD.getTag());
    tags.add(NotificationsEnum.SETTING_SENSITIVE.getTag());

    // this is a guest response
    if (signUpResponse == null) {
      tags.add(SharedPreferencesManager.REGISTERED_FALSE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED, SharedPreferencesManager.REGISTERED_FALSE);
    } else {
      tags.add(SharedPreferencesManager.REGISTERED_TRUE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED, SharedPreferencesManager.REGISTERED_TRUE);
    }

    sharedPreferencesManager.set(SharedPreferencesManager.LAST_NOTIFICATION, NotificationsEnum.LAST_GOOD.getTag());
    sharedPreferencesManager.set(SharedPreferencesManager.SETTINGS_NOTIFICATION, NotificationsEnum.SETTING_SENSITIVE.getTag());

    UAirship.shared().getPushManager().setTags(tags);
    UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    UAirship.shared().getPushManager().setPushEnabled(true);

    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.putExtra(LoginActivity.EMAIL, verifyEmailPayload.getUserName());
    intent.putExtra(LoginActivity.PASSWORD, password.getText().toString());
    intent.putExtra(LoginActivity.IS_DEEPLINK, true);
    startActivity(intent);
    getActivity().finish();
  }

  @Override
  public void registrationUnSuccessful(String error) {
    setContentView();
    showDialog(error);
  }
}
