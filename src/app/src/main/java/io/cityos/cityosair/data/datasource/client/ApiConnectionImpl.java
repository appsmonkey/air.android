package io.cityos.cityosair.data.datasource.client;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.cityos.cityosair.data.remote.RestApi;
import io.cityos.cityosair.data.remote.RetrofitClient;

@Singleton
public class ApiConnectionImpl implements ApiConnection {

  private RestApi restApi;
  private RetrofitClient client;

  @Inject
  ApiConnectionImpl(RetrofitClient client) {
    this.client = client;
    this.restApi = client.getClient().create(RestApi.class);

  }

  @Override
  public RestApi getClient() {
    return this.restApi;
  }


  @Override
  public int getTimeout() {
    return client.getTimeout();
  }

}
