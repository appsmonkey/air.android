package io.cityos.cityosair.data.datasource.client;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton public class HttpClientBuilderImpl implements HttpClientBuilder {

  @Inject HttpClientBuilderImpl() {
  }

  @Override public OkHttpClient.Builder getClientBuilder() {
    return new OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS);
  }
}
