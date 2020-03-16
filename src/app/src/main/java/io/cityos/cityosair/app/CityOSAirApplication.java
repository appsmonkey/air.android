package io.cityos.cityosair.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.urbanairship.UAirship;

import java.util.Locale;

import io.cityos.cityosair.BuildConfig;
import io.cityos.cityosair.R;
import io.cityos.cityosair.di.DaggerMainComponent;
import io.cityos.cityosair.di.MainComponent;
import io.cityos.cityosair.di.MainModule;
import io.cityos.cityosair.util.app.CityOSActivityLifecycleListener;
import io.fabric.sdk.android.Fabric;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Andrej on 07/02/2017.
 */

public class CityOSAirApplication extends Application {
  private static String TAG = CityOSAirApplication.class.getSimpleName();

  private MainComponent mainComponent;
  private static Context mContext;

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build());

      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build());
    }

    mainComponent =
            DaggerMainComponent.builder().mainModule(new MainModule(getApplicationContext())).build();

    mContext = getApplicationContext();

    Locale.setDefault(Locale.US);

    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }

    FacebookSdk.setApplicationId("FACEBOOK_KEY");
    FacebookSdk.sdkInitialize(this);

    //Realm
    Realm.init(this);

    RealmMigration migration = (realm, oldVersion, newVersion) -> {
      RealmSchema schema = realm.getSchema();

      for (long version = oldVersion + 1; version <= newVersion; version++) {
        switch ((int) version) {
          case 1:
            // migrate to version 1
            break;
        }
      }
    };

    // delete if migration needed.
    // move everything to room in refactoring
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("default3")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            //.migration(migration)
            .build();
    //        Realm.deleteRealm(realmConfiguration);
    Realm.setDefaultConfiguration(realmConfiguration);

    //Callbacks for events
    //used for refreshing data when coming from background
    this.registerActivityLifecycleCallbacks(new CityOSActivityLifecycleListener());

    //Register fonts
    ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                    new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/texgyreadventor-regular.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .setFontMapper(font -> font)
                            .build()))
            .build());

    SharedPreferences prefs = this.getSharedPreferences(
            "io.cityos.cityosair", Context.MODE_PRIVATE);

    String notificationPreferences = prefs.getString("notificationPreferences", null);
    if (notificationPreferences == null) {
      FirebaseMessaging.getInstance().subscribeToTopic("broadcast_dev");
      prefs.edit().putString("notificationPreferences", "notificationPreferences").apply();
    }
  }

  public MainComponent getMainComponent() {
    return mainComponent;
  }

  public static Context getAppContext() {
    return mContext;
  }
}
