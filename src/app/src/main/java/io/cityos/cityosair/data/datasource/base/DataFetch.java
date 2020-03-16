package io.cityos.cityosair.data.datasource.base;

import io.reactivex.Observable;

public interface DataFetch<A, B> {
  Observable<A> fetch(B payload);
}
