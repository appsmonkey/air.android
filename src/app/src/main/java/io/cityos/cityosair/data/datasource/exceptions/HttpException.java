package io.cityos.cityosair.data.datasource.exceptions;

import io.cityos.cityosair.data.datasource.base.Base;
import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HttpException extends RxException {
  protected String url;
  protected Response response;
  protected int code;

  private Retrofit retrofit;
  private Base<Object> bodyData;

  public HttpException(String url, Response response, int code, Retrofit retrofit,
      Throwable cause) {
    super(cause);
    this.url = url;
    this.response = response;
    this.retrofit = retrofit;
    this.bodyData = new Base<>();
    this.code = code;
    try {
      this.bodyData = getErrorBodyAs(bodyData.getClass());
    } catch (Throwable e) {
      this.addSuppressed(e);
    }
  }

  private <T> T getErrorBodyAs(Class<T> type) throws IOException {
    if (response == null || response.errorBody() == null) {
      return null;
    }
    Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
    return converter.convert(response.errorBody());
  }

  public String getUrl() {
    return url;
  }

  public Response getResponse() {
    return response;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public Base<Object> getBodyData() {
    return bodyData;
  }

  public int getCode() {
    return code;
  }
}
