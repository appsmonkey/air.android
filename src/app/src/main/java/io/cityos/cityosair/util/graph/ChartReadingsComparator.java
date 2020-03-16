package io.cityos.cityosair.util.graph;

import java.util.Comparator;

import io.cityos.cityosair.data.model.reading.ChartReading;

public class ChartReadingsComparator implements Comparator<ChartReading> {
  @Override
  public int compare(ChartReading reading1, ChartReading reading2) {
    return Long.compare(reading1.getDate(), reading2.getDate());
  }
}