package io.cityos.cityosair.ui.device.connectdevice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * First screen activity in add device workflow
 */
public class ConnectDeviceActivity extends AppCompatActivity {

  public static void show(Fragment fragment) {
    Intent intent = new Intent(fragment.getContext(), ConnectDeviceActivity.class);
    fragment.startActivityForResult(intent, Constants.SETTINGS_REQUEST_CODE);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_connect_device);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.a_layout, new ConnectDeviceFragment());
    ft.commit();
  }
}
