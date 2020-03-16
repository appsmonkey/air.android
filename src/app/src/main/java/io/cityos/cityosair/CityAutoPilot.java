package io.cityos.cityosair;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;

import io.cityos.cityosair.util.notification.CityOSAirshipListener;

/**
 * Autopilot that enables user notifications on first run.
 */
public class CityAutoPilot extends Autopilot {

  private static final String NO_BACKUP_PREFERENCES = "com.urbanairship.sample.no_backup";

  private static final String FIRST_RUN_KEY = "first_run";

  @Override
  public void onAirshipReady(@NonNull UAirship airship) {
    SharedPreferences preferences = UAirship.getApplicationContext().getSharedPreferences(NO_BACKUP_PREFERENCES, Context.MODE_PRIVATE);

    boolean isFirstRun = preferences.getBoolean(FIRST_RUN_KEY, true);
    if (isFirstRun) {
      preferences.edit().putBoolean(FIRST_RUN_KEY, false).apply();

      // Enable user notifications on first run
      airship.getPushManager().setUserNotificationsEnabled(true);
    }
    CityOSAirshipListener airshipListener = new CityOSAirshipListener();
    airship.getPushManager().addPushListener(airshipListener);
    airship.getPushManager().addPushTokenListener(airshipListener);
    airship.getPushManager().setNotificationListener(airshipListener);
    airship.getChannel().addChannelListener(airshipListener);
  }

  @Nullable
  @Override
  public AirshipConfigOptions createAirshipConfigOptions(@NonNull Context context) {
    return super.createAirshipConfigOptions(context);
  }

}
