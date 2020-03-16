package io.cityos.cityosair.util.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.urbanairship.UAirship;
import com.urbanairship.channel.AirshipChannelListener;
import com.urbanairship.push.NotificationActionButtonInfo;
import com.urbanairship.push.NotificationInfo;
import com.urbanairship.push.NotificationListener;
import com.urbanairship.push.PushListener;
import com.urbanairship.push.PushMessage;
import com.urbanairship.push.PushTokenListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.ui.notifications.NotificationsEnum;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;

public class CityOSAirshipListener  implements PushListener, NotificationListener, PushTokenListener, AirshipChannelListener {

  private static final String TAG = "AirshipListener";

  @Override
  public void onNotificationPosted(@NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification posted: " + notificationInfo);
  }

  @Override
  public boolean onNotificationOpened(@NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification opened: " + notificationInfo);

    // Return false here to allow Airship to auto launch the launcher
    // activity for foreground notification action buttons
    return false;
  }

  @Override
  public boolean onNotificationForegroundAction(@NonNull NotificationInfo notificationInfo, @NonNull NotificationActionButtonInfo actionButtonInfo) {
    Log.i(TAG, "Notification action: " + notificationInfo + " " + actionButtonInfo);

    // Return false here to allow Airship to auto launch the launcher
    // activity for foreground notification action buttons
    return false;
  }

  @Override
  public void onNotificationBackgroundAction(@NonNull NotificationInfo notificationInfo, @NonNull NotificationActionButtonInfo actionButtonInfo) {
    Log.i(TAG, "Notification action: " + notificationInfo + " " + actionButtonInfo);
  }

  @Override
  public void onNotificationDismissed(@NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification dismissed. Alert: " + notificationInfo.getMessage().getAlert() + ". Notification ID: " + notificationInfo.getNotificationId());
  }

  @Override
  public void onPushReceived(@NonNull PushMessage message, boolean notificationPosted) {
    Log.i(TAG, "Received push message. Alert: " + message.getAlert() + ". Posted notification: " + notificationPosted);
    SharedPreferences prefs = CityOSAirApplication.getAppContext().getSharedPreferences("io.cityos.cityosair", Context.MODE_PRIVATE);

    if (message.getPushBundle().getString("add") == null) {
      return;
    }
    List<String> tagsToAdd = Arrays.asList(message.getPushBundle().getString("add").split("\\|"));
    Set addSet;
    addSet = UAirship.shared().getPushManager().getTags();
    addSet.clear();
    addSet.addAll(tagsToAdd);
    addSet.add(prefs.getString(SharedPreferencesManager.SETTINGS_NOTIFICATION, NotificationsEnum.SETTING_SENSITIVE.getTag()));
    addSet.add(prefs.getString(SharedPreferencesManager.REGISTERED, SharedPreferencesManager.REGISTERED_FALSE));

    prefs.edit().putString(SharedPreferencesManager.LAST_NOTIFICATION, tagsToAdd.get(0)).apply();

    UAirship.shared().getPushManager().setTags(addSet);
  }

  @Override
  public void onChannelCreated(@NonNull String channelId) {
    Log.i(TAG, "Channel created " + channelId);
  }

  @Override
  public void onChannelUpdated(@NonNull String channelId) {
    Log.i(TAG, "Channel updated " + channelId);
  }

  @Override
  public void onPushTokenUpdated(@NonNull String token) {
    Log.i(TAG, "Push token updated " + token);
  }

}