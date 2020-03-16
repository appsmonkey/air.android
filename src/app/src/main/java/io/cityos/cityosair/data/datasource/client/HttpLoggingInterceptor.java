package io.cityos.cityosair.data.datasource.client;

import okhttp3.Interceptor;

public interface HttpLoggingInterceptor {
  okhttp3.logging.HttpLoggingInterceptor getInterceptor();
  Interceptor getResponseInterceptor();
  Interceptor getRequestInterceptor();
  void setTokens(String idToken, String accessToken, String refreshToken);
  void clearTokens();
}
