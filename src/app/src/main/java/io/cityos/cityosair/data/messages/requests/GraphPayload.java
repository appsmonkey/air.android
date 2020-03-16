package io.cityos.cityosair.data.messages.requests;

import io.cityos.cityosair.data.model.reading.Timeframe;

public class GraphPayload {
  String token;
  long from;
  String sensor;
  Timeframe timeframe;

  public GraphPayload(String token, long from, String sensor, Timeframe timeframe) {
    this.token = token;
    this.from = from;
    this.sensor = sensor;
    this.timeframe = timeframe;
  }

  public Timeframe getTimeframe() {
    return timeframe;
  }

  public String getToken() {
    return token;
  }

  public long getFrom() {
    return from;
  }

  public String getSensor() {
    return sensor;
  }
}
