package io.cityos.cityosair.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.util.app.DeepLinksHandler;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.ui.onboarding.entrypoint.EntryActivity;
import io.cityos.cityosair.util.app.AndroidUtils;
import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
  private static String TAG = SplashActivity.class.getSimpleName();

  @Inject
  HttpLoggingInterceptor httpLoggingInterceptor;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    Uri data = intent.getData();
    // if the data is not null this is either user signing up or resetting his password
    // open deep link with data
    if (data != null) {
      openDeepLink(data);
    } else {
      // otherwise normal lanuch
      ((CityOSAirApplication) getApplication()).getMainComponent().inject(this);
      User user = CacheUtil.getSharedCache().getUser();
      // if this is first start proceed with first launch flow
      if (AndroidUtils.isFirstLaunchSetupFor(this) || user == null) {
        EntryActivity.show(this);
        finish();
      }
      // if not set authentication tokens and start splash activity
      else {
        if (!user.isGuest()) {
          httpLoggingInterceptor.setTokens(user.getId_token(), user.getAccess_token(), user.getRefresh_token());
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    }
  }

  private void openDeepLink(Uri data) {
    DeepLinksHandler.handleDeepLinks(getSupportFragmentManager(), data);
  }
}
