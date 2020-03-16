package io.cityos.cityosair.ui.graph;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.reading.ChartReading;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.reading.Timeframe;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import io.cityos.cityosair.ui.aqi.AQIActivity;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.CityOSGradientBuilder;
import io.cityos.cityosair.util.graph.ChartReadingsComparator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class GraphFragment extends NewBaseFragment implements GraphView {

  @Inject GraphPresenter graphPresenter;

  @BindView(R.id.graph_top_layout) RelativeLayout graphTopLayout;
  @BindView(R.id.tv_graph_reading) TextView readingIdentifierTextView;
  @BindView(R.id.tv_graph_value) TextView readingValueTextView;
  @BindView(R.id.tv_graph_unit) TextView readingUnitTextView;
  @BindView(R.id.tv_graph_aqi) TextView graphAqiTextView;
  @BindView(R.id.btn_tab_month) Button btnTabMonth;
  @BindView(R.id.month_view) View monthView;
  @BindViews({ R.id.btn_tab_live, R.id.btn_tab_day, R.id.btn_tab_week, R.id.btn_tab_month })
  List<Button> tabButtons;

  @BindString(R.string.fetching_data) String strFetchingData;
  @BindString(R.string.unable_to_retrieve_data) String strUnableToRetrieve;
  @BindString(R.string.no_data) String strNoData;
  @BindString(R.string.aqi_great_title) String strGreat;
  @BindString(R.string.aqi_ok_title) String strOk;
  @BindString(R.string.aqi_sensitive_title) String strSensitiveBeware;
  @BindString(R.string.aqi_unhealthy_title) String strUnhealthy;
  @BindString(R.string.aqi_very_unhealthy_title) String strVeryUnhealthy;
  @BindString(R.string.aqi_hazardous_title) String strHazardous;

  private LineChart lineChart;
  private int selectedBtnId;
  private DeviceMeasurement deviceMeasurement;
  private String deviceId;
  private Timeframe timeframe;
  private boolean isDefaultDevice;
  private AQIEnum currentAQI = AQIEnum.AIR_PM10;

  @OnClick(R.id.btn_graph_close)
  void clickedCloseBtn() {
    getActivity().onBackPressed();
  }

  @OnClick(R.id.tv_graph_aqi)
  void gaugeClicked() {
    AQIActivity.show(getActivity(), currentAQI);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    graphPresenter.setView(this);

    deviceMeasurement = getArguments().getParcelable(GraphActivity.GRAPH_READING_KEY);
    deviceId = getArguments().getString(GraphActivity.GRAPH_DEVICE_ID_KEY);
    isDefaultDevice = getArguments().getBoolean(GraphActivity.GRAPH_DEVICE_DEFAULT);

    graphPresenter.checkIfGraphExists(deviceMeasurement.getSensorId(), deviceId);

    //Initial selected live tab
    clickedTabBtn(tabButtons.get(0));

    setUI();
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return graphPresenter;
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_graph;
  }

  private void setUI() {

    graphAqiTextView.setVisibility(View.GONE);

    readingIdentifierTextView.setText(deviceMeasurement.getName());

    double value = deviceMeasurement.getValue();
    // if the sensor is pressure convert to hPa
    if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.AIR_PRESSURE.getIdentifier())) {
      value = value / 100;
    }
    // if the sensor is temperature round the value
    else if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.AIR_TEMPERATURE.getIdentifier())
        || deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.DEVICE_TEMPERATURE.getIdentifier())) {
      value = Math.round(value);
    }
    if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.BATTERY_VOLTAGE.getIdentifier())) {
      readingValueTextView.setText(String.valueOf(value));
    } else {
      readingValueTextView.setText(String.valueOf((int)value));
    }

    // if the sensor is unknown, i.e. newly added sensor without strongly typed support in the code minimal support to show its value and unit
    if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.UNIDENTIFIED.getIdentifier())) {
      readingUnitTextView.setText(deviceMeasurement.getUnit());
    } else {
      readingUnitTextView.setText(deviceMeasurement.getReadingType().getUnitNotation());
    }

    // the separate logic for pm sensors types
    if (deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10
        || deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
      AQIEnum aqiType =
          deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10
                  ? AQIEnum.AIR_PM10
                  : AQIEnum.AIR_PM2P5;
      currentAQI = aqiType;
      AqiIndex aqi = AqiIndex.getAQIForTypeWithValue(deviceMeasurement.getValue(), aqiType);

      graphAqiTextView.setText(aqi.getTitle());
      graphAqiTextView.setTextColor(ContextCompat.getColor(getActivity(), aqi.getTextColorResource()));
      graphAqiTextView.setVisibility(View.VISIBLE);
    }
  }

  @OnClick({ R.id.btn_tab_live, R.id.btn_tab_day, R.id.btn_tab_week, R.id.btn_tab_month })
  void clickedTabBtn(Button tabBtn) {

    // if the same graph scope is selected again return
    if (selectedBtnId == tabBtn.getId()) return;

    createChart();

    selectedBtnId = tabBtn.getId();

    for (Button btn : tabButtons) {
      GradientDrawable drawable = (GradientDrawable) btn.getBackground();
      drawable.setColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    GradientDrawable btnDrawable = (GradientDrawable) tabBtn.getBackground();
    btnDrawable.setColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteSmoke));

    // set the timeframe depending on the selected data scope
    switch (selectedBtnId) {
      case R.id.btn_tab_live:
        timeframe = Timeframe.LIVE;
        break;
      case R.id.btn_tab_day:
        timeframe = Timeframe.DAY;
        break;
      case R.id.btn_tab_week:
        timeframe = Timeframe.WEEK;
        break;
      case R.id.btn_tab_month:
        timeframe = Timeframe.MONTH;
        break;
    }

    lineChart.setNoDataText(strFetchingData);
    lineChart.invalidate();

    graphPresenter.getGraphData(deviceId, deviceMeasurement.getSensorId(), timeframe, isDefaultDevice);
  }

  private void createChart() {
    if (lineChart != null) {
      graphTopLayout.removeView(lineChart);
      lineChart = null;
    }

    RelativeLayout.LayoutParams chartParams = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT);
    LineChart chart = new LineChart(getActivity());

    chartParams.addRule(RelativeLayout.BELOW, R.id.tv_graph_aqi);
    chartParams.addRule(RelativeLayout.ABOVE, R.id.graph_tab_layout);
    chart.setLayoutParams(chartParams);
    graphTopLayout.addView(chart);
    lineChart = chart;

    setChart();
  }

  private void setChart() {
    //Chart
    lineChart.setHardwareAccelerationEnabled(true);
    lineChart.getLegend().setEnabled(false);

    lineChart.getAxisLeft().setEnabled(true);
    lineChart.getAxisRight().setEnabled(true);

    //X Axis
    XAxis xAxis = lineChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setAvoidFirstLastClipping(true);
    xAxis.setDrawAxisLine(false);
    xAxis.setDrawGridLines(false);

    lineChart.getAxisLeft().setDrawAxisLine(false);
    lineChart.getAxisLeft().setDrawGridLines(true);
    lineChart.getAxisRight().setDrawAxisLine(false);

    //xAxis.setMultiLineLabel(true);

    //Maybe
    xAxis.setLabelCount(4, true);

    xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.graph_labels));
    lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(getActivity(), R.color.graph_labels));
    lineChart.getAxisRight().setTextColor(ContextCompat.getColor(getActivity(), R.color.graph_aqi_labels));

    xAxis.setTextSize(getDPSizeFromSP(12));
    //xAxis.setLabelRotationAngle(90.0f);
    lineChart.getAxisLeft().setTextSize(getDPSizeFromSP(13));
    lineChart.getAxisRight().setTextSize(getDPSizeFromSP(13));

    lineChart.getAxisRight().setGridLineWidth(1);
    lineChart.getAxisRight().setGridColor(ContextCompat.getColor(getActivity(), R.color.graph_line));

    lineChart.setScaleEnabled(false);
    lineChart.setDragEnabled(true);
    lineChart.setHighlightPerDragEnabled(true);
    lineChart.setHighlightPerTapEnabled(true);

    lineChart.setNoDataTextColor(ContextCompat.getColor(getActivity(), R.color.colorGraphSubtitle));
    Description desc = new Description();
    desc.setEnabled(false);
    lineChart.setDescription(desc);

    lineChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
    lineChart.getAxisRight().setXOffset(10);
    lineChart.getAxisRight().setYOffset(-20);

    lineChart.setExtraBottomOffset(15);
  }

  private float getDPSizeFromSP(int size) {
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    float px = size * getResources().getDisplayMetrics().scaledDensity;
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
  }

  @Override
  public void graphLoaded(List<ChartReading> chartReadingList) {
    Collections.sort(chartReadingList, new ChartReadingsComparator());
    setChartData(chartReadingList);
  }

  @Override public void graphError() {
    if (lineChart != null) {
      lineChart.setNoDataText(strUnableToRetrieve);
      lineChart.invalidate();
    }
  }

  private void setChartData(List<ChartReading> data) {
    if (data == null) {
      lineChart.setNoDataText(strUnableToRetrieve);
      lineChart.invalidate();
      return;
    }

    if (data.size() == 0) {
      lineChart.setNoDataText(strNoData);
      lineChart.invalidate();
      return;
    }

    if (data.size() == 1) {
      ChartReading existing = data.get(0);
      ChartReading point = new ChartReading(existing.getValue(), existing.getDate(), timeframe);
      data.add(point);
    }

    if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.AIR_PRESSURE.getIdentifier())) {
      for (ChartReading reading : data) {
        reading.setValue(reading.getValue() / 100);
      }
    }

    //Temporary Bug fix

    Timeframe currentTimeframe = timeframe;

    if (currentTimeframe == Timeframe.MONTH
        || currentTimeframe == Timeframe.DAY
        || currentTimeframe == Timeframe.WEEK) {
      lineChart.setExtraRightOffset(25);
    }

    List<Entry> entries = new ArrayList<>();
    List<String> timestamps = new ArrayList<>();
    List<String> markerLabels = new ArrayList<>();

    double dataSize = data.size();
    for (int i = 0; i < dataSize; i++) {
      ChartReading chartReading = data.get(i);
      chartReading.setTimeframe(timeframe);
      float value = (float) chartReading.getValue();
      if (deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.BATTERY_VOLTAGE) {
        Entry entry = new Entry(i, value);
        entries.add(entry);
      } else {
        Entry entry = new Entry(i, (int)value);
        entries.add(entry);
      }
      timestamps.add(chartReading.getXLabel());
      markerLabels.add(chartReading.getMarkerLabel());
    }

    XAxis xAxis = lineChart.getXAxis();

    xAxis.setValueFormatter(new TimestampsXAxisValueFormatter(timestamps, currentTimeframe));
    lineChart.setMarker(new GraphMarker(getActivity(), R.layout.graph_marker,
            deviceMeasurement.getReadingType().getUnitNotation(),
            markerLabels,
            lineChart,
            deviceMeasurement.getReadingType())
    );

    LineDataSet dataSet = new LineDataSet(entries, "");
    dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    dataSet.setLineWidth(0);
    dataSet.setDrawCircles(false);
    dataSet.setDrawValues(false);
    dataSet.setDrawHorizontalHighlightIndicator(false);
    dataSet.setDrawVerticalHighlightIndicator(true);
    dataSet.setHighLightColor(ContextCompat.getColor(getActivity(), R.color.white));
    dataSet.setDrawFilled(true);
    CityOSGradientBuilder.context = getActivity();

    if (deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10
        || deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
      double max = dataSet.getYMax() + (dataSet.getYMax() * 0.1);
      int numberOfGradientsToLeave = setupLabelsForPmData(0, max);
      if (dataSet.getYMax() > 10) {
        dataSet.setFillDrawable(CityOSGradientBuilder
            .getPMGradient(numberOfGradientsToLeave, (int)dataSet.getYMax(), deviceMeasurement.getReadingType().getEnumValue()));
      } else {
        dataSet.setFillDrawable(CityOSGradientBuilder.getGreatGradient());
      }
      if (deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
        if (dataSet.getYMax() > 251) {
          lineChart.setVisibleYRangeMaximum(251, YAxis.AxisDependency.LEFT);
        }
      } else {
        if (dataSet.getYMax() > 425) {
          lineChart.setVisibleYRangeMaximum(425, YAxis.AxisDependency.LEFT);
        }
      }
    } else {
      lineChart.getAxisLeft().setLabelCount(4, true);
      lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
        if (deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.BATTERY_VOLTAGE.getIdentifier())) {
          int scale = (int) Math.pow(10, 1);
          double newValue = (double) Math.round(value * scale) / scale;
          return newValue + "";
        } else {
          DecimalFormat decimalFormat = new DecimalFormat("#");
          return decimalFormat.format(value);
        }
      });
      int yMin = (int)dataSet.getYMin();
      yMin = yMin == 0 ? yMin - 2 : (int) (yMin - (yMin * 0.2));
      lineChart.getAxisLeft().setAxisMinimum(yMin);
      lineChart.getAxisRight().setEnabled(false);
      dataSet.setFillDrawable(CityOSGradientBuilder.getDefaultGradient());
    }

    lineChart.setData(new LineData(dataSet));
//    lineChart.notifyDataSetChanged();
//    lineChart.invalidate();

    if (dataSet.getEntryCount() > 10) {
      float xScale = dataSet.getEntryCount() / 10;
      lineChart.zoom(xScale, 0.8f, 0.0f, 0.0f);
      lineChart.moveViewToX(dataSet.getEntryCount());
    }

    lineChart.getXAxis().setGranularity(1.0f);
    lineChart.animateXY(2, 2);
  }

  static class TimestampsXAxisValueFormatter implements IAxisValueFormatter {

    private List<String> timestamps;
    private Timeframe timeframe;

    TimestampsXAxisValueFormatter(List<String> values, Timeframe timeframe) {
      this.timestamps = values;
      this.timeframe = timeframe;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
      // "value" represents the position of the label on the axis (x or y)
      int cleanedValue = Math.abs((int) value);

      if (cleanedValue >= timestamps.size()) {
        return timestamps.get(timestamps.size() - 1);
      }

      String timestamp = timestamps.get(cleanedValue);

      if (timeframe == Timeframe.WEEK || timeframe == Timeframe.MONTH) {
        String date = timestamp.substring(0, 6).trim();
        return date;
      } else if (timeframe == Timeframe.DAY) {
        try {
          String time = timestamp.substring(timestamp.length() - 6, timestamp.length() - 1);
          SimpleDateFormat simpleDateFormat24 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
          SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("hha", Locale.ENGLISH);
          Date date24 = simpleDateFormat24.parse(time);
          String time12 = simpleDateFormat12.format(date24).toLowerCase();
          return time12;
        } catch (Exception e) {
          e.printStackTrace();
          return "";
        }
      } else {
        return (int)(timestamps.size() - value) + "m ago";
      }
    }
  }

  private List<Double> pm25xValue = Arrays.asList(0.0, 12.0, 35.5, 55.5, 150.5, 250.5, 250.6);
  private List<Double> pm10xValue = Arrays.asList(0.0, 54.0, 154.0, 254.0, 354.0, 424.0, 425.0);

  private int xCounter = 0;

  private int setupLabelsForPmData(double min, double max) {
    List<Double> xValues = deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10
            ? pm10xValue
            : pm25xValue;

    double xMax = 0;
    xCounter = 0;
    for (double num : xValues) {
      if (num > max && xMax < max) {
        xMax = num;
      }
    }

    if (xMax == 0.0) {
      xMax = max;
    }

    List<Double> filteredX = new ArrayList<>();

    for (double value : xValues) {
      if (value >= min && value <= xMax) {
        filteredX.add(value);
      }
    }

    int numberOfLabels = filteredX.size();

    lineChart.getAxisRight().setAxisMinimum((int)min);
    lineChart.getAxisRight().setAxisMaximum((int)max);
    lineChart.getAxisLeft().setAxisMinimum((int)min);
    lineChart.getAxisLeft().setAxisMaximum((int)max);

    lineChart.getAxisRight().setValueFormatter((value, axis) -> {

      if (xCounter >= numberOfLabels) {
        xCounter = 0;
      }

      double num = filteredX.get(xCounter);

      xCounter++;

      if (num == filteredX.get(filteredX.size() - 1)) {
        return "";
      }

      if (deviceMeasurement.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10) {

        if (num == 0.0) {
          return strGreat;
        } else if (num == 54.0) {
          return strOk;
        } else if (num == 154.0) {
          return strSensitiveBeware;
        } else if (num == 254.0) {
          return strUnhealthy;
        } else if (num == 354.0) {
          return strVeryUnhealthy;
        } else if (num == 424.0) {
          return strHazardous;
        } else {
          return strHazardous;
        }
      } else {

        if (num == 0.0) {
          return strGreat;
        } else if (num == 12.0) {
          return strOk;
        } else if (num == 35.5) {
          return strSensitiveBeware;
        } else if (num == 55.5) {
          return strUnhealthy;
        } else if (num == 150.5) {
          return strVeryUnhealthy;
        } else if (num == 250.5) {
          return strHazardous;
        } else {
          return strHazardous;
        }
      }
    });

    lineChart.getAxisLeft().setLabelCount(numberOfLabels, true);
    lineChart.getAxisRight().setLabelCount(numberOfLabels, true);

    return numberOfLabels;
  }

  @Override
  public void showMonthLabel(boolean show) {
    btnTabMonth.setVisibility(show ? View.VISIBLE : View.GONE);
    monthView.setVisibility(show ? View.VISIBLE : View.GONE);
  }
}
