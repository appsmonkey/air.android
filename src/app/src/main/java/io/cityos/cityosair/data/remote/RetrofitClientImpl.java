package io.cityos.cityosair.data.remote;

import com.google.gson.Gson;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.datasource.client.HttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RetrofitClientImpl implements RetrofitClient {

  private final HttpClient httpClient;

  @Inject
  public RetrofitClientImpl(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public Retrofit getClient() {
    return buildClient()
        .client(this.httpClient.getHttpClient())
        .build();
  }

  private Retrofit.Builder buildClient() {
    return new Retrofit.Builder().baseUrl(Constants.API_BASE_PROD_URL)
        .addConverterFactory(GsonConverterFactory.create(new Gson()))
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
  }

  @Override
  public int getTimeout() {
    return httpClient.getHttpClient().connectTimeoutMillis();
  }
}
