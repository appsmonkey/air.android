package io.cityos.cityosair.util.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.City;
import io.cityos.cityosair.util.PredefinedDevices;
import io.cityos.cityosair.util.cache.CacheUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andrej on 07/02/2017.
 */

public class AndroidUtils {
  /**
   * @param view View to animate
   * @param toVisibility Visibility at the end of animation
   * @param toAlpha Alpha at the end of animation
   * @param duration Animation duration in ms
   */
  public static void animateView(final View view, final int toVisibility, float toAlpha,
      int duration) {

    boolean show = toVisibility == View.VISIBLE;
    if (show) {
      view.setAlpha(0);
      view.bringToFront();
    }
    view.setVisibility(View.VISIBLE);
    view.animate()
        .setDuration(duration)
        .alpha(show ? toAlpha : 0)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            view.setVisibility(toVisibility);
          }
        });
  }

  public static void showLoadingView(final View view, String loadingText) {

    TextView textView = (TextView) view.findViewById(R.id.progress_text);
    textView.setText(loadingText);

    animateView(view, View.VISIBLE, 0.4f, 200);
  }

  public static void hideLoadingView(final View view) {
    animateView(view, View.INVISIBLE, 0, 200);
  }

  public static void setToolbar(AppCompatActivity activity, Toolbar toolbar, String title,
                                Boolean shouldDisplayHome) {
    activity.setSupportActionBar(toolbar);
    activity.getSupportActionBar().setTitle(title);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(shouldDisplayHome);
    activity.getSupportActionBar().setHomeButtonEnabled(shouldDisplayHome);
  }

  public static boolean isFirstLaunchSetupFor(AppCompatActivity activity) {
    SharedPreferences prefs = activity.getPreferences(MODE_PRIVATE);
    boolean firstRun = prefs.getBoolean(Constants.FIRST_RUN_KEY, true);

    if (firstRun) {

      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean(Constants.FIRST_RUN_KEY, false);
      editor.commit();

      CacheUtil.getSharedCache().setCity(City.sarajevoCity);

      //Used so that Sarajevo Air is created
      CacheUtil.getSharedCache().saveDevices(new PredefinedDevices(true).getPredefinedDevices());

      return true;
    } else {

      City currentCity = CacheUtil.getSharedCache().getCity();
      if (currentCity == null) {

        CacheUtil.getSharedCache().setCity(City.sarajevoCity);

        CacheUtil.getSharedCache().saveDevices(new PredefinedDevices(true).getPredefinedDevices());
      }

      //refreshUserData();

      return false;
    }
  }
}
