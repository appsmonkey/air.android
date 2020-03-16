package io.cityos.cityosair.ui.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.map.MapZone;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.util.cache.CacheUtil;

public class InfoMapFragment extends Fragment {

  @BindView(R.id.tv_name) TextView tvName;
  @BindView(R.id.tv_message) TextView tvMessage;
  @BindView(R.id.tv_reading_pm25_value) TextView tvPm25Value;
  @BindView(R.id.tv_reading_pm10_value) TextView tvPm10Value;
  @BindView(R.id.tv_reading_temp_value) TextView tvTempValue;
  @BindView(R.id.temp_container) LinearLayout tempContainer;
  @BindView(R.id.img_arrow) ImageView arrowImage;

  boolean isClickable = false;
  private Device device;
  private MapZone mapZone;
  private DeviceMeasurementCollection deviceMeasurementCollection;

  void updateValues(MapZone mapZone, AqiIndex aqiIndex, Double pm25Value, Double pm10Value, Double tempValue, Boolean isActive) {
    this.mapZone = mapZone;
    this.isClickable = false;

    updateValues(mapZone.getZoneName(), aqiIndex, pm25Value, pm10Value, tempValue, 0, isActive);
  }

  void updateValues(Device device, AqiIndex aqiIndex, Double pm25Value, Double pm10Value, Double tempValue, boolean isClickable, Boolean isActive) {

    this.device = device;
    this.isClickable = isClickable;

    updateValues(device.getName(), aqiIndex, pm25Value, pm10Value, tempValue, device.getTimestamp(), isActive);
  }

  public void updateValues(DeviceMeasurementCollection deviceMeasurementCollection,
                           AqiIndex aqiIndex, Double pm25Value, Double pm10Value,
                           Double tempValue, boolean isClickable, Boolean isActive) {

    this.deviceMeasurementCollection = deviceMeasurementCollection;
    this.isClickable = isClickable;

    Device device = CacheUtil.getSharedCache().getDeviceById(deviceMeasurementCollection.getDeviceId());

    updateValues(deviceMeasurementCollection.getDeviceName(), aqiIndex, pm25Value, pm10Value, tempValue, device.getTimestamp(), isActive);
  }

  private void updateValues(String name, AqiIndex aqiIndex, Double pm25, Double pm10, Double temp, long timestamp, Boolean isActive) {
    if (isClickable) {
      arrowImage.setVisibility(View.VISIBLE);
    } else {
      arrowImage.setVisibility(View.GONE);
    }

    tvName.setText(name);

    AqiIndex pm10AqiIndex = AqiIndex.getAQIForTypeWithValue(pm10, AQIEnum.AIR_PM10);
    AqiIndex pm25AqiIndex = AqiIndex.getAQIForTypeWithValue(pm25, AQIEnum.AIR_PM2P5);
    int aqi10TextColor = ContextCompat.getColor(getActivity(), pm10AqiIndex.getTextColorResource());
    int aqi25TextColor = ContextCompat.getColor(getActivity(), pm25AqiIndex.getTextColorResource());
    int textColor;
    if (isActive != null && !isActive) {
      pm10AqiIndex = AqiIndex.getAQIForLevel("");
      pm25AqiIndex = AqiIndex.getAQIForLevel("");
      aqi10TextColor = ContextCompat.getColor(getActivity(), pm10AqiIndex.getTextColorResource());
      aqi25TextColor = ContextCompat.getColor(getActivity(), pm25AqiIndex.getTextColorResource());
    }
    if (pm25AqiIndex.getLevel() > pm10AqiIndex.getLevel()) {
      textColor = aqi25TextColor;
    } else {
      textColor = aqi10TextColor;
    }

    tvMessage.setTextColor(textColor);
    tvPm10Value.setTextColor(aqi10TextColor);
    tvPm25Value.setTextColor(aqi25TextColor);

    if (pm25 == null || pm25 == -1 || (isActive != null && !isActive)) {
      pm25 = 0.0;
      tvMessage.setTextColor(Color.BLACK);
      tvPm25Value.setTextColor(Color.BLACK);
    }

    if (pm10 == null || pm10 == -1 || (isActive != null && !isActive)) {
      pm10 = 0.0;
      tvMessage.setTextColor(Color.BLACK);
      tvPm10Value.setTextColor(Color.BLACK);
    }

    if (isActive != null && !isActive) {
      temp = null;
      tvMessage.setTextColor(Color.BLACK);
      tvPm10Value.setTextColor(Color.BLACK);
      tvPm25Value.setTextColor(Color.BLACK);
    }

    String aqiZero = getResources().getString(R.string.aqi_zero_title);
    String aqiTitle = getResources().getString(aqiIndex.getTitle());
    if (aqiZero.equals(aqiTitle) && temp != null && temp == 0.0  || (isActive != null && !isActive)) {
      Calendar calendar = Calendar.getInstance();
      long diffMilliseconds = calendar.getTimeInMillis() - timestamp;
      long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diffMilliseconds);
      int seconds = (int)diffSeconds % 60;
      diffSeconds /= 60;
      int minutes = (int)diffSeconds % 60;
      diffSeconds /= 60;
      int hours = (int)diffSeconds % 24;
      diffSeconds /= 24;
      int days = (int)diffSeconds;
      String message = "Device is offline";
      tvMessage.setText(message);
    } else {
      tvMessage.setText(aqiIndex.getTitle());
    }
    if (aqiZero.equals(aqiTitle)) {
      tvMessage.setTextColor(Color.BLACK);
      tvPm10Value.setTextColor(Color.BLACK);
      tvPm25Value.setTextColor(Color.BLACK);
    }
    tvPm25Value.setText(MessageFormat.format("{0}", pm25.intValue()));
    tvPm10Value.setText(MessageFormat.format("{0}", pm10.intValue()));

    if (temp == null) {
      tempContainer.setVisibility(View.GONE);
    } else {
      tempContainer.setVisibility(View.VISIBLE);
      tvTempValue.setText(MessageFormat.format("{0}", temp.intValue()));
    }
  }

  @OnClick(R.id.info_window_container)
  void infoWindowClicked() {
    if (isClickable) {
      if (clickListener != null) {
        if (device != null) {
          clickListener.infoWindowClicked(device);
        } else {
          clickListener.infoWindowClicked(deviceMeasurementCollection);
        }
      }
    }
  }

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private MapEventHandler clickListener;

  public InfoMapFragment() {
    // Required empty public constructor
  }

  public InfoMapFragment setClickListener(
      MapEventHandler clickListener) {
    this.clickListener = clickListener;
    return this;
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment InfoMapFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static InfoMapFragment newInstance(String param1, String param2) {
    InfoMapFragment fragment = new InfoMapFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_info_map, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    //if (mContext instanceof MapFragment.MapEventHandler) {
    //  mListener = (MapFragment.MapEventHandler) mContext;
    //} else {
    //  throw new RuntimeException(mContext.toString()
    //      + " must implement MapEventHandler");
    //}
  }

  @Override
  public void onDetach() {
    super.onDetach();
    clickListener = null;
  }
}
