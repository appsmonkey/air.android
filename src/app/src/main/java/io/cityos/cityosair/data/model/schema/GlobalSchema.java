package io.cityos.cityosair.data.model.schema;

import java.util.Map;

public class GlobalSchema {

  Map<String, SchemaSensorType> schema;

  public GlobalSchema() {
  }

  public Map<String, SchemaSensorType> getSchema() {
    return schema;
  }

  public GlobalSchema setSchema(
      Map<String, SchemaSensorType> schema) {
    this.schema = schema;
    return this;
  }
}
