package io.cityos.cityosair.data.datasource.client;

import io.cityos.cityosair.data.remote.RestApi;

public interface ApiConnection {

  RestApi getClient();
  int getTimeout();
}
