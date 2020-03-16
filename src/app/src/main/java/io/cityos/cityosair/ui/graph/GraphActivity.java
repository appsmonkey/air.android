package io.cityos.cityosair.ui.graph;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class GraphActivity extends AppCompatActivity {

  public static final String GRAPH_READING_KEY = "GRAPH_READING_KEY";
  public static final String GRAPH_DEVICE_ID_KEY = "GRAPH_DEVICE_ID_KEY";
  public static final String GRAPH_DEVICE_DEFAULT = "GRAPH_DEVICE_DEFAULT";

  DeviceMeasurement deviceMeasurement;
  String deviceId;
  boolean defaultDevice;

  public static void show(Context context, DeviceMeasurement deviceMeasurement, String deviceId,
      boolean isDefaultDevice) {
    Intent intent = new Intent(context, GraphActivity.class);

    intent.putExtra(GRAPH_READING_KEY, deviceMeasurement);
    intent.putExtra(GRAPH_DEVICE_ID_KEY, deviceId);
    intent.putExtra(GRAPH_DEVICE_DEFAULT, isDefaultDevice);

    context.startActivity(intent);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graph);

    // when coming from
    deviceMeasurement = getIntent().getParcelableExtra(GRAPH_READING_KEY);
    deviceId = getIntent().getStringExtra(GRAPH_DEVICE_ID_KEY);
    defaultDevice = getIntent().getBooleanExtra(GRAPH_DEVICE_DEFAULT, false);

    Bundle bundle = new Bundle();
    bundle.putParcelable(GRAPH_READING_KEY, deviceMeasurement);
    bundle.putString(GRAPH_DEVICE_ID_KEY, deviceId);
    bundle.putBoolean(GRAPH_DEVICE_DEFAULT, defaultDevice);
    GraphFragment graphFragment = new GraphFragment();
    graphFragment.setArguments(bundle);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.graph_fragment_container, graphFragment);
    ft.commit();
  }
}
