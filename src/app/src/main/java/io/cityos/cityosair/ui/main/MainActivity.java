package io.cityos.cityosair.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.ui.main.adapter.CustomFragmentStatePagerAdapter;
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan;
import io.github.inflationx.calligraphy3.TypefaceUtils;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.fragment_container, new MainTabFragment());
    ft.commit();
  }

  @Override protected void onStart() {
    super.onStart();
  }
}
