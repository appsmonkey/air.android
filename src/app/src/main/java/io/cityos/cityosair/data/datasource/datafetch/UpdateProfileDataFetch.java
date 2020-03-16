package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.messages.requests.UpdateProfilePayload;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class UpdateProfileDataFetch extends AbstractDataFetch<Object, UpdateProfilePayload> {

  private HttpLoggingInterceptor httpLoggingInterceptor;
  private SchemaDataFetch schemaDataFetch;

  @Inject UpdateProfileDataFetch(HttpLoggingInterceptor httpLoggingInterceptor,
      SchemaDataFetch schemaDataFetch) {
    this.httpLoggingInterceptor = httpLoggingInterceptor;
    this.schemaDataFetch = schemaDataFetch;
  }

  @Override protected Observable<Object> fetchData(UpdateProfilePayload payload) {

    return api.getClient().updateProfile(payload);
  }
}
