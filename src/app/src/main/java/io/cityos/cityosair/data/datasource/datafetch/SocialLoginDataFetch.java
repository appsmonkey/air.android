package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.messages.requests.SocialLoginPayload;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class SocialLoginDataFetch extends AbstractDataFetch<User, SocialLoginPayload> {

  private HttpLoggingInterceptor httpLoggingInterceptor;
  private SchemaDataFetch schemaDataFetch;

  @Inject SocialLoginDataFetch(HttpLoggingInterceptor httpLoggingInterceptor,
      SchemaDataFetch schemaDataFetch) {
    this.httpLoggingInterceptor = httpLoggingInterceptor;
    this.schemaDataFetch = schemaDataFetch;
  }

  @Override protected Observable<User> fetchData(SocialLoginPayload payload) {

    return api.getClient().socialLogin(payload)
        .map(Base::getData)
        .doOnNext(user -> {
          user.setGuest(false);
          CacheUtil.getSharedCache().save(user);
          httpLoggingInterceptor.setTokens(user.getId_token(), user.getAccess_token(),
              user.getRefresh_token());
        })
        .flatMap(user -> schemaDataFetch.fetch(null).map(stringSchemaSensorTypeMap -> user));
  }
}
