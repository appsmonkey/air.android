package io.cityos.cityosair.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.urbanairship.UAirship;
import io.cityos.cityosair.BuildConfig;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.notifications.NotificationsEnum;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.ui.map.MapDataSingleton;
import io.cityos.cityosair.ui.notifications.NotificationActivity;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {

  @Inject HttpLoggingInterceptor httpLoggingInterceptor;
  @Inject MapDataSingleton mapDataSingleton;
  @Inject SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progress_overlay) View progressOverlay;
  @BindView(R.id.settings_tv_notifications_enabled) TextView notificationsEnabledTextView;
  @BindView(R.id.settings_tv_notify_detail) TextView notifyDetailTextView;
  @BindView(R.id.settings_login_btn) Button loginBtn;
  @BindView(R.id.settings_logout_btn) Button logoutBtn;
  @BindView(R.id.bad_air_alerts_desc) TextView badAirAlertsDesc;

  @BindString(R.string.settings_title) String strSettings;
  @BindString(R.string.not_subscribed_to_alerts) String strNotSubscribed;
  @BindString(R.string.unable_to_retrieve_alerts) String strUnableToRetrieveAlerts;
  @BindString(R.string.settings_notifications_on) String strOn;
  @BindString(R.string.settings_notifications_off) String strOff;
  @BindString(R.string.choose_a_city) String strChooseACity;
  @BindString(R.string.cancel) String strCancel;
  @BindString(R.string.settings_notifications_detail) String strReceivingBadAirAlerts;
  @BindString(R.string.settings_notifications_detail_disabled) String strReceiveBadAirAlerts;

  private boolean notificationsEnabled;

  public static void show(Context context) {
    Intent intent = new Intent(context, SettingsActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.bind(this);

    ((CityOSAirApplication) getApplication()).getMainComponent().inject(this);

    AndroidUtils.setToolbar(this, toolbar, strSettings, true);

    String setNotification =
        sharedPreferencesManager.get(SharedPreferencesManager.SETTINGS_NOTIFICATION,
            NotificationsEnum.SETTING_SENSITIVE.getTag());

    for (NotificationsEnum notificationsEnum : NotificationsEnum.values()) {
      if (setNotification.equals(notificationsEnum.getTag())) {
        notifyDetailTextView.setText(notificationsEnum.getFullName());
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    notificationsEnabled = UAirship.shared().getPushManager().getUserNotificationsEnabled();

    if (notificationsEnabled) {
      notificationsEnabledTextView.setText(strOn);
      badAirAlertsDesc.setText(strReceivingBadAirAlerts);
    } else {
      badAirAlertsDesc.setText(strReceiveBadAirAlerts);
      notificationsEnabledTextView.setText(strOff);
    }

    User user = CacheUtil.getSharedCache().getUser();

    if (user != null && !user.isGuest()) {
      loginBtn.setVisibility(View.GONE);
      logoutBtn.setVisibility(View.VISIBLE);
    } else {
      loginBtn.setVisibility(View.VISIBLE);
      logoutBtn.setVisibility(View.GONE);
    }
  }

  @OnClick(R.id.notifications_enable_layout)
  public void notificationsEnableClicked() {

    if (notificationsEnabled) {
      notificationsEnabledTextView.setText(strOff);
      notifyDetailTextView.setText(strNotSubscribed);
      badAirAlertsDesc.setText(strReceiveBadAirAlerts);

      UAirship.shared().getPushManager().setPushEnabled(false);
      UAirship.shared().getPushManager().setUserNotificationsEnabled(false);
    } else {
      UAirship.shared().getPushManager().setPushEnabled(true);
      UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
      notificationsEnabledTextView.setText(strOn);
      badAirAlertsDesc.setText(strReceivingBadAirAlerts);
    }

    notificationsEnabled = !notificationsEnabled;
  }

  @OnClick(R.id.notifications_notify_layout)
  public void notifyMeClicked() {
    NotificationActivity.show(this);
  }

  @OnClick(R.id.settings_login_btn)
  public void loginClicked() {
    LoginActivity.show(this);
    finish();
  }

  @OnClick(R.id.settings_logout_btn)
  public void logoutClicked() {

    LoginManager.getInstance().logOut();

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_API_ID)
        .requestEmail()
        .build();

    GoogleSignInClient mGoogleSignInClient =
        GoogleSignIn.getClient(this, gso);

    mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
      @Override public void onComplete(@NonNull Task<Void> task) {
        mapDataSingleton.logout();
        CacheUtil.getSharedCache().logoutUser();
        loginBtn.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.GONE);

        sharedPreferencesManager.clear();
        UAirship.shared().getPushManager().setUserNotificationsEnabled(false);
        UAirship.shared().getPushManager().setPushEnabled(false);

        httpLoggingInterceptor.clearTokens();

        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
      }
    });
  }
}
