//  package io.cityos.cityosair.util.notification;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.annotation.NonNull;
//
//import com.urbanairship.AirshipReceiver;
//import com.urbanairship.UAirship;
//import com.urbanairship.push.PushMessage;
//import io.cityos.cityosair.ui.notifications.NotificationsEnum;
//import io.cityos.cityosair.util.cache.SharedPreferencesManager;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//
//public class CityOSAirshipReceiver extends AirshipReceiver {
//
//  private static final String TAG = "SampleAirshipReceiver";
//
//  @Override
//  protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message,
//                                boolean notificationPosted) {
//
//    SharedPreferences prefs = context.getSharedPreferences(
//        "io.cityos.cityosair", Context.MODE_PRIVATE);
//
//    if (message.getPushBundle().getString("add") == null) {
//      return;
//    }
//    List<String> tagsToAdd = Arrays.asList(message.getPushBundle().getString("add").split("\\|"));
//    Set addSet;
//    addSet = UAirship.shared().getPushManager().getTags();
//    addSet.clear();
//    addSet.addAll(tagsToAdd);
//    addSet.add(
//        prefs.getString(SharedPreferencesManager.SETTINGS_NOTIFICATION,
//            NotificationsEnum.SETTING_SENSITIVE.getTag()));
//    addSet.add(prefs.getString(SharedPreferencesManager.REGISTERED,
//        SharedPreferencesManager.REGISTERED_FALSE));
//
//    prefs.edit().putString(SharedPreferencesManager.LAST_NOTIFICATION, tagsToAdd.get(0)).apply();
//
//    UAirship.shared().getPushManager().setTags(addSet);
//  }
//}