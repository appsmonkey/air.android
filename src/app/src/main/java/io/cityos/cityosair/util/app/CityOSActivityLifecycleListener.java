package io.cityos.cityosair.util.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Andrej on 11/03/2017.
 */

public class CityOSActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

  private int activitiesStarted = 0;

  @Override
  public void onActivityResumed(Activity activity) {
    Log.i("Lifecycle", "In onActivityResumed");

    if (activitiesStarted == 0) {
      Log.i("Lifecycle", "Executed onActivityResumed == 0");
      //AndroidUtils.refreshUserData();
    }

    activitiesStarted++;
  }

  @Override
  public void onActivityStopped(Activity activity) {
    Log.i("Lifecycle", "In onActivityStopped");

    activitiesStarted--;

    if (activitiesStarted == 0) {
      Log.i("Lifecycle", "Executed onActivityStopped == 0");
    }
  }

  //Ignore
  @Override
  public void onActivityPaused(Activity activity) {

  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

  }

  @Override
  public void onActivityStarted(Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {

  }
}
