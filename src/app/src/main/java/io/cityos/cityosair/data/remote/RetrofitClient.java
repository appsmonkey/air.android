package io.cityos.cityosair.data.remote;

import retrofit2.Retrofit;

public interface RetrofitClient {
  Retrofit getClient();

  int getTimeout();
}
