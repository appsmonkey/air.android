package io.cityos.cityosair.ui.onboarding.entrypoint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import io.cityos.cityosair.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class EntryActivity extends AppCompatActivity {

  public static void show(Context context) {
    Intent intent = new Intent(context, EntryActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_entry);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.a_layout, new EntryFragment());
    ft.commit();
  }
}
