package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.messages.requests.GraphPayload;
import io.cityos.cityosair.data.model.reading.ChartReading;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;

public class AverageGraphDataFetch extends AbstractDataFetch<List<ChartReading>, GraphPayload> {

  @Inject AverageGraphDataFetch() {
  }

  @Override protected Observable<List<ChartReading>> fetchData(GraphPayload payload) {
    return api.getClient()
        .getAverageGraphReadings(payload.getTimeframe().getRawValue().equals("") ? "live"
            : payload.getTimeframe().getRawValue(), payload.getFrom(), payload.getSensor())
        .map(Base::getData);
  }
}
