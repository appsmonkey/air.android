package io.cityos.cityosair.data.datasource.client;

import okhttp3.OkHttpClient;

public interface HttpClientBuilder {
  OkHttpClient.Builder getClientBuilder();
}
