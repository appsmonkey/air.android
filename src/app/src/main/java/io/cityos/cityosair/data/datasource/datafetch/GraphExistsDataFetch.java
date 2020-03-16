package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.messages.requests.ChartExistsPayload;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class GraphExistsDataFetch extends AbstractDataFetch<Boolean, ChartExistsPayload> {

  @Inject GraphExistsDataFetch() {
  }

  @Override
  protected Observable<Boolean> fetchData(ChartExistsPayload chartExistsPayload) {
    return api.getClient()
        .checkIfChartExists(chartExistsPayload.getChart(), chartExistsPayload.getSensor(),
            chartExistsPayload.getFrom(), chartExistsPayload.getDevice())
        .map(Base::getData);
  }
}
