package io.cityos.cityosair.data.datasource.client;

import io.cityos.cityosair.BuildConfig;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.Request;

@Singleton public class HttpLoggingInterceptorImpl implements HttpLoggingInterceptor {

  private String id_token = "";
  private String access_token = "";
  private String refresh_token = "";

  @Inject HttpLoggingInterceptorImpl() {

  }

  @Override public okhttp3.logging.HttpLoggingInterceptor getInterceptor() {
    okhttp3.logging.HttpLoggingInterceptor interceptor =
        new okhttp3.logging.HttpLoggingInterceptor(
            message -> {/*Log.d("API", message != null ? message : "");  */
              int maxLogSize = 2000;
              for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                android.util.Log.d("API", message.substring(start, end));
              }
            });
    interceptor.setLevel(BuildConfig.DEBUG ? okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        : okhttp3.logging.HttpLoggingInterceptor.Level.NONE);
    return interceptor;
  }

  @Override public Interceptor getResponseInterceptor() {
    return chain -> chain.proceed(chain.request());
  }

  @Override public Interceptor getRequestInterceptor() {

    return chain -> {
      String mimeType = "application/json";
      Request.Builder builder = chain.request()
          .newBuilder()
          .addHeader("Authorization", this.id_token)
          .addHeader("AccessToken", this.access_token)
          .addHeader("content-type", mimeType);

      return chain.proceed(builder.build());
    };
  }

  public void setTokens(String idToken, String accessToken, String refreshToken) {
    this.id_token = idToken;
    this.access_token = accessToken;
    if (refreshToken != null && !refreshToken.isEmpty()) {
      this.refresh_token = refreshToken;
    }
  }

  public void clearTokens() {
    this.id_token = "";
    this.access_token = "";
    this.refresh_token = "";
  }
}
