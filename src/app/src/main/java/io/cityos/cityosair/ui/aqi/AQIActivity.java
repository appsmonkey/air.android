package io.cityos.cityosair.ui.aqi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.AqiIndex;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import java.util.List;

public class AQIActivity extends AppCompatActivity {

  private static final String AQI_TYPE_KEY = "AQI_TYPE_KEY";

  @BindView(R.id.tv_aqi_title) TextView titleTextView;
  @BindView(R.id.tv_aqi_subtitle) TextView subtitleTextView;
  @BindView(R.id.aqi_recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.divider_view) View dividerView;

  private AqiAdapter adapter;

  public static void show(Context context, AQIEnum type) {
    Intent intent = new Intent(context, AQIActivity.class);
    intent.putExtra(AQI_TYPE_KEY, type);
    context.startActivity(intent);
  }

  @OnClick(R.id.aqi_back_btn)
  public void clickedBackBtn() {
    onBackPressed();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_aqi);

    ButterKnife.bind(this);

    AQIEnum currentType = (AQIEnum) getIntent().getSerializableExtra(AQI_TYPE_KEY);

    String title;
    String subtitle;
    List<AqiIndex> aqiList;

    if (currentType == AQIEnum.AIR_PM10) {
      title = getResources().getText(R.string.title_pm10).toString();
      subtitle = getResources().getText(R.string.subtitle_pm10).toString();
    } else {
      title = getResources().getText(R.string.title_pm2_5).toString();
      subtitle = getResources().getText(R.string.subtitle_pm2_5).toString();
    }

    titleTextView.setText(title);
    subtitleTextView.setText(subtitle);

    adapter = new AqiAdapter(AqiIndex.getAllAQIsForType(currentType));
    recyclerView.setAdapter(adapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(layoutManager);

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();

        dividerView.setVisibility(firstVisibleItem == 0 ? View.INVISIBLE : View.VISIBLE);
      }
    });
  }
}
