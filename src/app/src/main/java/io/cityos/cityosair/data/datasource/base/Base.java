package io.cityos.cityosair.data.datasource.base;

import java.util.ArrayList;
import java.util.List;

public class Base<DataResponse> {

  private Boolean success;
  private List<Error> errors = new ArrayList<>();
  private DataResponse data;

  public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public List<Error> getErrors() {
    return errors;
  }

  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  public DataResponse getData() {
    return data;
  }

  public void setData(DataResponse data) {
    this.data = data;
  }
}
