package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.data.model.schema.GlobalSchema;
import io.cityos.cityosair.data.model.schema.GlobalSchemaParser;
import io.cityos.cityosair.data.model.schema.SchemaSensorType;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;

public class GuestSchemaDataFetch extends AbstractDataFetch<Map<String, SchemaSensorType>, Void> {

  @Inject GuestSchemaDataFetch() {
  }

  @Override protected Observable<Map<String, SchemaSensorType>> fetchData(Void payload) {
    return api.getClient().getSchema()
        .map(Base::getData)
        .doOnNext(stringSchemaSensorTypeMap -> {
          GlobalSchema globalSchema = new GlobalSchema();
          globalSchema.setSchema(stringSchemaSensorTypeMap);

          GlobalSchemaParser globalSchemaParser = new GlobalSchemaParser(globalSchema);

          CacheUtil.getSharedCache().save(globalSchemaParser.getReadingsSchema());
          User user = new User();
          user.setGuest(true);
          CacheUtil.getSharedCache().save(user);
        });
  }
}
