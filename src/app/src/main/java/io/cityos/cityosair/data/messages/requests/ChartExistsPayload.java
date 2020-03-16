package io.cityos.cityosair.data.messages.requests;

public class ChartExistsPayload {
  String chart;
  String sensor;
  long from;
  String device;

  public ChartExistsPayload(String chart, String sensor, long from, String device) {
    this.chart = chart;
    this.sensor = sensor;
    this.from = from;
    this.device = device;
  }

  public String getChart() {
    return chart;
  }

  public ChartExistsPayload setChart(String chart) {
    this.chart = chart;
    return this;
  }

  public String getSensor() {
    return sensor;
  }

  public ChartExistsPayload setSensor(String sensor) {
    this.sensor = sensor;
    return this;
  }

  public long getFrom() {
    return from;
  }

  public ChartExistsPayload setFrom(long from) {
    this.from = from;
    return this;
  }

  public String getDevice() {
    return device;
  }

  public ChartExistsPayload setDevice(String device) {
    this.device = device;
    return this;
  }
}
