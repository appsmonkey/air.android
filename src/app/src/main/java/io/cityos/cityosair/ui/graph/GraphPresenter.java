package io.cityos.cityosair.ui.graph;

import io.cityos.cityosair.data.messages.requests.ChartExistsPayload;
import io.cityos.cityosair.data.messages.requests.GraphPayload;
import io.cityos.cityosair.data.model.reading.ChartReading;
import io.cityos.cityosair.data.model.reading.Timeframe;
import io.cityos.cityosair.data.datasource.datafetch.AverageGraphDataFetch;
import io.cityos.cityosair.data.datasource.datafetch.GraphDataFetch;
import io.cityos.cityosair.data.datasource.datafetch.GraphExistsDataFetch;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.app.CityOSDateUtils;
import io.reactivex.observers.DisposableObserver;
import java.util.List;
import javax.inject.Inject;

public class GraphPresenter extends BasePresenter<GraphView> {

  private GraphDataFetch graphDataFetch;
  private GraphExistsDataFetch graphExistsDataFetch;
  private AverageGraphDataFetch averageGraphDataFetch;

  @Inject
  public GraphPresenter(GraphDataFetch graphDataFetch,
      AverageGraphDataFetch averageGraphDataFetch,
      GraphExistsDataFetch graphExistsDataFetch) {
    this.graphDataFetch = graphDataFetch;
    this.graphExistsDataFetch = graphExistsDataFetch;
    this.averageGraphDataFetch = averageGraphDataFetch;
  }

  void checkIfGraphExists(String sensor, String deviceId) {

    long timestamp = CityOSDateUtils.getLastThreeWeeksTimestampInSeconds();

    getCompositeDisposable().add(
        graphExistsDataFetch.fetch(new ChartExistsPayload("month", sensor, timestamp, deviceId))
            .subscribeWith(new DisposableObserver<Boolean>() {
              @Override
              public void onNext(Boolean aBoolean) {
                getView().showMonthLabel(aBoolean);
              }
              @Override

              public void onError(Throwable e) {
                getView().graphError();
              }

              @Override
              public void onComplete() { }
            }));
  }

  void getGraphData(String deviceId, String sensor, Timeframe timeframe, boolean isDefaultDevice) {

    long timestamp = 0;

    switch (timeframe) {
      case MONTH:
        timestamp = CityOSDateUtils.getLastMonthTimestampInSeconds();
        break;
      case WEEK:
        timestamp = CityOSDateUtils.getLastWeekTimestampInSeconds();
        break;
      case DAY:
        timestamp = CityOSDateUtils.getPreviousDayTimestampInSeconds();
        break;
      case LIVE:
        timestamp = CityOSDateUtils.getPreviousFiveHoursTimestampInSeconds();
        break;
    }

    GraphPayload graphPayload = new GraphPayload(deviceId, timestamp, sensor, timeframe);

    if (isDefaultDevice) {
      getCompositeDisposable()
          .add(averageGraphDataFetch.fetch(graphPayload)
          .subscribeWith(new GraphDisposableObserver()));
    } else {
      getCompositeDisposable()
          .add(graphDataFetch.fetch(graphPayload)
          .subscribeWith(new GraphDisposableObserver()));
    }
  }

  private class GraphDisposableObserver extends DisposableObserver<List<ChartReading>> {

    @Override
    public void onNext(List<ChartReading> chartReadings) {
      getView().graphLoaded(chartReadings);
    }

    @Override
    public void onError(Throwable e) {
      getView().graphError();
    }

    @Override
    public void onComplete() { }
  }
}
