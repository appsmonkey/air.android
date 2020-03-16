package io.cityos.cityosair.data.model.schema;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SchemaSensorType {
  private String name;
  private String unit;
  @SerializedName("steps") private List<Step> steps;
  @SerializedName("default")
  private String defaultMeasure;

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public String getDefaultMeasure() {
    return defaultMeasure;
  }
}