package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.entities.ValidateEmailEntity;
import io.cityos.cityosair.data.messages.requests.ValidateEmailPayload;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class ValidateEmailDataFetch
    extends AbstractDataFetch<ValidateEmailEntity, ValidateEmailPayload> {

  @Inject ValidateEmailDataFetch() {
  }

  @Override protected Observable<ValidateEmailEntity> fetchData(ValidateEmailPayload payload) {

    return api.getClient().validateEmail(payload)
        .map(Base::getData);
  }
}
