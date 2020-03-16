package io.cityos.cityosair.util.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.util.cache.CacheUtil;
import okhttp3.Cache;

/**
 * TODO: document your custom view class.
 */
public class MapSegmentControl extends LinearLayout {

  public interface MapSegmentEventListener {
    void segmentChanged(int index);
  }

  @BindView(R.id.btn_outdoor) AppCompatTextView btnOutdoor;
  @BindView(R.id.btn_indoor) AppCompatTextView btnIndoor;
  @BindView(R.id.btn_mine) AppCompatTextView btnMine;

  private MapSegmentEventListener mapSegmentEventListener;
  private Drawable boxImg;
  private int darkColor;
  private int lightColor;

  GradientDrawable btnIndoorBackground;
  GradientDrawable btnOutdoorBackground;
  GradientDrawable btnMineBackground;

  public int currentIndex = 0;

  public MapSegmentControl(Context context) {
    super(context);
    initControl(context);
  }

  public MapSegmentControl(Context context, AttributeSet attrs) {
    super(context, attrs);
    initControl(context);
  }

  public MapSegmentControl(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initControl(context);
  }

  private void initControl(Context context) {

    //        LayoutInflater inflater = (LayoutInflater)
    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    //        inflater.inflate(R.layout.map_segment_control, this);

    inflate(context, R.layout.map_segment_control, this);

    ButterKnife.bind(this);

    boxImg = ContextCompat.getDrawable(context, R.drawable.btn_box_item);

    lightColor = ContextCompat.getColor(context, R.color.graph_line);
    darkColor = ContextCompat.getColor(context, R.color.map_fill_zero);

    btnIndoorBackground = (GradientDrawable) btnIndoor.getBackground();
    btnOutdoorBackground = (GradientDrawable) btnOutdoor.getBackground();
    btnMineBackground = (GradientDrawable) btnMine.getBackground();

    toggleUI(0);
  }

  public void setMapSegmentEventListener(MapSegmentEventListener eventListener) {
    this.mapSegmentEventListener = eventListener;
  }

  public void toggleUI(int index) {

    currentIndex = index;

    switch (index) {
      case 0:
        //btnIndoor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnIndoorBackground.setColor(lightColor);
        btnIndoor.setTextColor(darkColor);

        //btnMine.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnMineBackground.setColor(lightColor);
        btnMine.setTextColor(darkColor);

        //btnOutdoor.setCompoundDrawablesWithIntrinsicBounds(boxImg, null, null, null);
        btnOutdoorBackground.setColor(darkColor);
        btnOutdoor.setTextColor(lightColor);
        break;
      case 1:
        //btnOutdoor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnOutdoorBackground.setColor(lightColor);
        btnOutdoor.setTextColor(darkColor);

        //btnMine.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnMineBackground.setColor(lightColor);
        btnMine.setTextColor(darkColor);

        //btnIndoor.setCompoundDrawablesWithIntrinsicBounds(boxImg, null, null, null);
        btnIndoorBackground.setColor(darkColor);
        btnIndoor.setTextColor(lightColor);
        break;
      default:
        //btnOutdoor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnOutdoorBackground.setColor(lightColor);
        btnOutdoor.setTextColor(darkColor);

        //btnIndoor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnIndoorBackground.setColor(lightColor);
        btnIndoor.setTextColor(darkColor);

        //btnMine.setCompoundDrawablesWithIntrinsicBounds(boxImg, null, null, null);
        btnMineBackground.setColor(darkColor);
        btnMine.setTextColor(lightColor);
        break;
    }
  }

  @OnClick(R.id.btn_outdoor)
  public void outdoorClicked() {

    toggleUI(0);

    if (mapSegmentEventListener != null) {
      mapSegmentEventListener.segmentChanged(0);
    }
  }

  @OnClick(R.id.btn_indoor)
  public void indoorClicked() {

    toggleUI(1);

    if (mapSegmentEventListener != null) {
      mapSegmentEventListener.segmentChanged(1);
    }
  }

  @OnClick(R.id.btn_mine) public void mineClicked() {
    toggleUI(2);

    if (mapSegmentEventListener != null) {
      mapSegmentEventListener.segmentChanged(2);
    }
  }
}
