package io.cityos.cityosair.data.datasource.client;

import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton public class HttpClientImpl implements HttpClient {

  private final HttpLoggingInterceptor interceptor;
  private final HttpClientBuilder builder;

  @Inject
  public HttpClientImpl(HttpLoggingInterceptor interceptor, HttpClientBuilder builder) {
    this.interceptor = interceptor;
    this.builder = builder;
  }

  @Override
  public OkHttpClient getHttpClient() {
    return builder.getClientBuilder()
        .addInterceptor(this.interceptor.getResponseInterceptor())
        .addInterceptor(this.interceptor.getRequestInterceptor())
        .addInterceptor(this.interceptor.getInterceptor())
        .build();
  }
}
