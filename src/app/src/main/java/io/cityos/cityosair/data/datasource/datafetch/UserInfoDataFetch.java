package io.cityos.cityosair.data.datasource.datafetch;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.model.UserInfo;
import io.cityos.cityosair.data.datasource.base.AbstractDataFetch;
import io.reactivex.Observable;
import javax.inject.Inject;

public class UserInfoDataFetch extends AbstractDataFetch<UserInfo, UserInfo> {

  @Inject UserInfoDataFetch() {
  }

  @Override protected Observable<UserInfo> fetchData(UserInfo payload) {
    return api.getClient().getUserInfoNew()
        .map(Base::getData);
  }
}
