package io.cityos.cityosair.ui.onboarding.loginfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import io.cityos.cityosair.R;
import io.cityos.cityosair.ui.onboarding.entrypoint.EntryActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {

  public static String EMAIL = "email";
  public static String PASSWORD = "password";
  public static String IS_DEEPLINK = "is_deeplink";

  private String email;
  private String password;
  private Boolean isDeeplink = false;

  public static void show(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      email = bundle.getString(EMAIL, "");
      password = bundle.getString(PASSWORD, "");
      isDeeplink = bundle.getBoolean(IS_DEEPLINK, false);
    }

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.a_layout, new LoginFragment());
    ft.commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
      getSupportFragmentManager().popBackStackImmediate();
    } else {
      Intent intent = new Intent(LoginActivity.this, EntryActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Boolean getIsDeeplink() {
    return isDeeplink;
  }

  public void setIsDeeplink(Boolean value) {
    isDeeplink = value;
  }
}
