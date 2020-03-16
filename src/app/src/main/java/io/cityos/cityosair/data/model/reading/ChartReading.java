package io.cityos.cityosair.data.model.reading;

import io.cityos.cityosair.util.app.CityOSDateUtils;
import java.util.Date;

public class ChartReading {

  private double value;
  private long date;
  private Timeframe timeframe;

  public ChartReading(double value, long date, Timeframe timeframe) {
    this.value = value;
    this.date = date;
    this.timeframe = timeframe;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public long getDate() {
    return date * 1000L;
  }

  public ChartReading setDate(long date) {
    this.date = date;
    return this;
  }

  public Timeframe getTimeframe() {
    return timeframe;
  }

  public void setTimeframe(Timeframe timeframe) {
    this.timeframe = timeframe;
  }

  public double getValue() {
    return value;
  }

  public String getXLabel() {
    if (timeframe == Timeframe.LIVE) {
      return CityOSDateUtils.getTimeAgo(new Date(date * 1000L));
    } else {
      return CityOSDateUtils.getRelativeDateTimeForTimestamp(date);
    }
  }

  public String getMarkerLabel() {
    return CityOSDateUtils.getRelativeDateTimeForTimestamp(date);
  }
}
