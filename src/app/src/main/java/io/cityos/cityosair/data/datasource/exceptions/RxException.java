package io.cityos.cityosair.data.datasource.exceptions;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class RxException extends Throwable {
  public RxException(String detailMessage, Throwable cause) {
    super(detailMessage, cause);
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public RxException(String detailMessage, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(detailMessage, cause, enableSuppression, writableStackTrace);
  }

  public RxException(Throwable cause) {
    super(cause);
  }

  public RxException() {
  }

  public RxException(String detailMessage) {
    super(detailMessage);
  }
}
