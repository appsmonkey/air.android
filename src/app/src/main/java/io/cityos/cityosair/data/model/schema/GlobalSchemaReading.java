package io.cityos.cityosair.data.model.schema;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;

public class GlobalSchemaReading extends RealmObject {

  private String id;
  private String name;
  private String unit;
  @SerializedName("steps") private RealmList<Step> steps;
  @SerializedName("default") private String defaultValue;

  public GlobalSchemaReading() {
  }

  public String  getId() {
    return id;
  }

  public GlobalSchemaReading setId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public GlobalSchemaReading setName(String name) {
    this.name = name;
    return this;
  }

  public String getUnit() {
    return unit;
  }

  public GlobalSchemaReading setUnit(String unit) {
    this.unit = unit;
    return this;
  }

  public RealmList<Step> getSteps() {
    return steps;
  }

  public GlobalSchemaReading setSteps(
      RealmList<Step> steps) {
    this.steps = steps;
    return this;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public GlobalSchemaReading setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }
}
