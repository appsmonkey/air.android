package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.schema.GlobalSchema;
import io.cityos.cityosair.data.model.schema.GlobalSchemaParser;
import io.cityos.cityosair.data.model.schema.SchemaSensorType;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;

public class SchemaDataFetch extends AbstractDataFetch<Map<String, SchemaSensorType>, Void> {

  @Inject SchemaDataFetch() {
  }

  @Override protected Observable<Map<String, SchemaSensorType>> fetchData(Void payload) {
    return api.getClient().getSchema()
        .map(Base::getData)
        .doOnNext(stringSchemaSensorTypeMap -> {
          GlobalSchema globalSchema = new GlobalSchema();
          globalSchema.setSchema(stringSchemaSensorTypeMap);

          GlobalSchemaParser globalSchemaParser = new GlobalSchemaParser(globalSchema);

          CacheUtil.getSharedCache().save(globalSchemaParser.getReadingsSchema());
        });
  }
}
