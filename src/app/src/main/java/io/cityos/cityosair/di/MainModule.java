package io.cityos.cityosair.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.cityos.cityosair.data.datasource.client.ApiConnection;
import io.cityos.cityosair.data.datasource.client.ApiConnectionImpl;
import io.cityos.cityosair.data.datasource.client.HttpClient;
import io.cityos.cityosair.data.datasource.client.HttpClientBuilder;
import io.cityos.cityosair.data.datasource.client.HttpClientBuilderImpl;
import io.cityos.cityosair.data.datasource.client.HttpClientImpl;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptorImpl;
import io.cityos.cityosair.data.remote.RetrofitClient;
import io.cityos.cityosair.data.remote.RetrofitClientImpl;
import io.cityos.cityosair.util.network.GsonService;
import io.cityos.cityosair.util.network.GsonServiceImpl;
import io.cityos.cityosair.util.NumberUtils;
import io.cityos.cityosair.util.NumberUtilsImpl;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;
import io.cityos.cityosair.util.cache.SharedPreferencesManagerImpl;

@Module
public class MainModule {
  Context context;

  public MainModule(Context context) {
    this.context = context;
  }

  @Provides
  @Singleton
  Context getContext() {
    return context;
  }

  @Provides
  @Singleton
  RetrofitClient getRetrofitClient(RetrofitClientImpl retrofitClient) {
    return retrofitClient;
  }

  @Provides
  @Singleton
  HttpClient getHttpClient(HttpClientImpl httpClient) {
    return httpClient;
  }

  @Provides
  @Singleton
  HttpClientBuilder getHttpClientBuilder(HttpClientBuilderImpl httpClientBuilder) {
    return httpClientBuilder;
  }

  @Provides
  @Singleton
  HttpLoggingInterceptor getHttpLoggingInterceptor(HttpLoggingInterceptorImpl httpLoggingInterceptor) {
    return httpLoggingInterceptor;
  }

  @Provides
  @Singleton
  ApiConnection getApiConnection(ApiConnectionImpl apiConnection) {
    return apiConnection;
  }

  @Provides
  @Singleton
  GsonService getGsonService(GsonServiceImpl gsonService) {
    return gsonService;
  }

  @Provides
  @Singleton
  NumberUtils getNumberUtils() {
    return new NumberUtilsImpl();
  }

  @Provides
  @Singleton
  SharedPreferencesManager getSharedPreferencesManager(SharedPreferencesManagerImpl sharedPreferencesManager) {
    return sharedPreferencesManager;
  }
}
