package io.cityos.cityosair.ui.graph;

import io.cityos.cityosair.data.model.reading.ChartReading;
import io.cityos.cityosair.ui.map.BaseView;
import java.util.List;

public interface GraphView extends BaseView {
  void graphLoaded(List<ChartReading> chartReadingList);
  void graphError();
  void showMonthLabel(boolean show);
}
