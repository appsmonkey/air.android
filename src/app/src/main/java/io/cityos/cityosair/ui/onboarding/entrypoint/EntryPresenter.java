package io.cityos.cityosair.ui.onboarding.entrypoint;

import io.cityos.cityosair.data.datasource.datafetch.GuestSchemaDataFetch;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.reactivex.observers.DisposableObserver;
import javax.inject.Inject;

public class EntryPresenter extends BasePresenter<EntryView> {

  GuestSchemaDataFetch guestSchemaDataFetch;

  @Inject EntryPresenter(GuestSchemaDataFetch guestSchemaDataFetch) {
    this.guestSchemaDataFetch = guestSchemaDataFetch;
  }

  public void getGuestDevicesAndSchema() {
    getView().setLoadingView();

    getCompositeDisposable().add(
        guestSchemaDataFetch.fetch(null).subscribeWith(new DisposableObserver<Object>() {

          @Override public void onNext(Object o) {
            getView().setContentView();
            getView().loginSuccessful(null);
          }

          @Override public void onError(Throwable e) {
            getView().setContentView();
            //genericError();
          }

          @Override public void onComplete() {

          }
        })
    );
  }
}
