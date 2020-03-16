package io.cityos.cityosair.ui.notifications;

import androidx.annotation.StringRes;

import io.cityos.cityosair.R;

public enum NotificationsEnum {

  SETTING_GOOD("setting_Good", R.string.notif_alerts_good),
  SETTING_SENSITIVE("setting_Sensitive", R.string.notif_alerts_sensitive),
  SETTING_UNHEALTHY("setting_Unhealthy", R.string.notif_alerts_unhealthy),
  SETTING_VERY_UNHEALTHY("setting_VeryUnhealthy", R.string.notif_alerts_very_unhealthy),
  SETTING_HAZARDOUS("setting_Hazardous", R.string.notif_alerts_hazardous),
  LAST_GOOD("last_Good", R.string.notif_alerts_good),
  LAST_SENSITIVE("last_Sensitive", R.string.notif_alerts_sensitive),
  LAST_UNHEALTHY("last_Unhealthy", R.string.notif_alerts_unhealthy),
  LAST_VERY_UNHEALTHY("last_VeryUnhealthy", R.string.notif_alerts_very_unhealthy),
  LAST_HAZARDOUS("last_Hazardous", R.string.notif_alerts_hazardous);

  private String tag;
  @StringRes
  private int fullName;

  NotificationsEnum(String tag, @StringRes int fullName) {
    this.tag = tag;
    this.fullName = fullName;
  }

  public String getTag() {
    return tag;
  }

  public int getFullName() {
    return fullName;
  }
}
