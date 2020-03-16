package io.cityos.cityosair.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;
import com.urbanairship.UAirship;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class NotificationActivity extends AppCompatActivity {
  private static String TAG = NotificationActivity.class.getSimpleName();

  @Inject SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.sb_alerts_range)  VerticalRangeSeekBar alertsSeekBar;

  @BindString(R.string.notif_alerts_title) String strAirAlerts;

  public static void show(Context context) {
    Intent intent = new Intent(context, NotificationActivity.class);
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
    setContentView(R.layout.activity_notification);

    ButterKnife.bind(this);

    AndroidUtils.setToolbar(this, toolbar, strAirAlerts, true);

    ((CityOSAirApplication) getApplication()).getMainComponent().inject(this);

    String setNotification =  sharedPreferencesManager.get(SharedPreferencesManager.SETTINGS_NOTIFICATION,
            NotificationsEnum.SETTING_SENSITIVE.getTag());

    setAlertsSeekBarPosition(setNotification);

    setAlertsSeekBarClickHandler();
  }

  private void setAlertsSeekBarPosition(String notification) {
    if (notification.equals(NotificationsEnum.SETTING_SENSITIVE.getTag())) {
      alertsSeekBar.setProgress(100);
    } else if (notification.equals(NotificationsEnum.SETTING_UNHEALTHY.getTag())) {
      alertsSeekBar.setProgress(66);
    } else if (notification.equals(NotificationsEnum.SETTING_VERY_UNHEALTHY.getTag())) {
      alertsSeekBar.setProgress(33);
    } else {
      alertsSeekBar.setProgress(0);
    }
  }

  private void setAlertsSeekBarClickHandler() {
    alertsSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
      @Override
      public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
        Log.d(TAG, "Left value: " + leftValue + ", right value: " + rightValue);
        if ((int)leftValue ==  0|| (int)leftValue == 33 || (int)leftValue == 66 || (int)leftValue == 100) {
          int value = (int)leftValue;
          if (value == 0) {
            value = 0;
          } else if (value == 33) {
            value = 1;
          } else if (value == 66) {
            value = 2;
          } else if (value == 100) {
            value = 3;
          }

          alertValueChanged(value);
        }
      }

      @Override
      public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

      }

      @Override
      public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

      }
    });
  }

  public void alertValueChanged(Integer value) {

    String lastNotification =
        sharedPreferencesManager.get(SharedPreferencesManager.LAST_NOTIFICATION,
            NotificationsEnum.LAST_GOOD.getTag());

    String settingsNotification;

    switch (value) {
      case 2:
        settingsNotification = NotificationsEnum.SETTING_UNHEALTHY.getTag();
        break;
      case 1:
        settingsNotification = NotificationsEnum.SETTING_VERY_UNHEALTHY.getTag();
        break;
      case 0:
        settingsNotification = NotificationsEnum.SETTING_HAZARDOUS.getTag();
        break;
      case 3:
      default:
        settingsNotification = NotificationsEnum.SETTING_SENSITIVE.getTag();
        break;
    }

     sharedPreferencesManager.set(SharedPreferencesManager.SETTINGS_NOTIFICATION, settingsNotification);

    Set tagsSet;
    tagsSet = UAirship.shared().getPushManager().getTags();
    tagsSet.clear();
    tagsSet.add(lastNotification);
     tagsSet.add(settingsNotification);
    tagsSet.add(sharedPreferencesManager.get(SharedPreferencesManager.REGISTERED, SharedPreferencesManager.REGISTERED_FALSE));
    UAirship.shared().getPushManager().setTags(tagsSet);

//    for (SwitchCompat switchCompat : switches) {
//      if (switchCompat.getId() != toggledSwitch.getId()) {
//        switchCompat.setChecked(false);
//      }
//    }
  }
}
