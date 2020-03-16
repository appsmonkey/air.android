package io.cityos.cityosair.data.datasource.base;

import io.cityos.cityosair.data.messages.requests.RefreshTokenPayload;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.data.datasource.client.ApiConnection;
import io.cityos.cityosair.data.datasource.client.HttpLoggingInterceptor;
import io.cityos.cityosair.data.datasource.exceptions.RetrofitException;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public abstract class AbstractDataFetch<A, B> implements DataFetch<A, B> {
  @Inject
  HttpLoggingInterceptor httpLoggingInterceptor;
  @Inject
  public ApiConnection api;

  @Override
  public Observable<A> fetch(B payload) {

    return fetchData(payload).onErrorResumeNext(error -> {
      if (error instanceof RetrofitException && ((RetrofitException) error).getCode() == 401) {
        return api.getClient()
            .refreshToken(
                new RefreshTokenPayload(CacheUtil.getSharedCache().getUser().getRefresh_token()))
            .map(userBase1 -> {
              User user = userBase1.getData();
              User databaseUser = CacheUtil.getSharedCache().getUser();
              databaseUser.setId_token(user.getId_token());
              databaseUser.setAccess_token(user.getAccess_token());
              CacheUtil.getSharedCache().save(databaseUser);
              httpLoggingInterceptor.setTokens(databaseUser.getId_token(),
                  databaseUser.getAccess_token(), databaseUser.getRefresh_token());
              return user;
            })
            .doOnNext(user -> {
              User databaseUser = CacheUtil.getSharedCache().getUser();
              databaseUser.setId_token(user.getId_token());
              databaseUser.setAccess_token(user.getAccess_token());
              CacheUtil.getSharedCache().save(databaseUser);
              httpLoggingInterceptor.setTokens(databaseUser.getId_token(),
                  databaseUser.getAccess_token(), databaseUser.getRefresh_token());
            })
            .subscribeOn(Schedulers.trampoline())
            .flatMap(userBase -> fetchData(payload));
      } else {
        return Observable.error(error);
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  protected abstract Observable<A> fetchData(B payload);
}
