package io.cityos.cityosair.data.model.schema;

import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalSchemaParser {

  private List<GlobalSchemaReading> readings = new ArrayList<>();

  public GlobalSchemaParser(GlobalSchema schema) {
    Map<String, SchemaSensorType> values = schema.getSchema();

    if (values == null) {
      return;
    }

    for (Map.Entry<String, SchemaSensorType> entry : values.entrySet()) {
      String key = entry.getKey();
      SchemaSensorType schemaSensorType = entry.getValue();

      GlobalSchemaReading globalSchemaReading = new GlobalSchemaReading();

      if (schemaSensorType.getSteps() != null) {
        RealmList<Step> steps = new RealmList<>(
            schemaSensorType.getSteps().toArray(new Step[schemaSensorType.getSteps().size()]));
        globalSchemaReading.setSteps(steps);
      }

      globalSchemaReading.setId(key);
      globalSchemaReading.setDefaultValue(schemaSensorType.getDefaultMeasure());
      globalSchemaReading.setName(schemaSensorType.getName());
      globalSchemaReading.setUnit(schemaSensorType.getUnit());

      this.readings.add(globalSchemaReading);
    }
  }

  public List<GlobalSchemaReading> getReadingsSchema() {
    return this.readings;
  }
}
