package io.cityos.cityosair.data.datasource.exceptions;

import java.io.IOException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitException extends Throwable {

  private String url;
  private Response response;
  private int code;

  public RetrofitException(String string, Response response, Retrofit retrofit) {
    this.url = string;
    this.response = response;
    this.code = response.code();
  }

  public RetrofitException(IOException ioException) {

  }

  public RetrofitException(Throwable throwable) {

  }

  public int getCode() {
    return code;
  }

  public Response getResponse() {
    return response;
  }
}
