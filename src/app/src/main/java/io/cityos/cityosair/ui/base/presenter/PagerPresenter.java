package io.cityos.cityosair.ui.base.presenter;

import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.map.BaseView;

public class PagerPresenter<T extends BaseView> extends BasePresenter<T> implements
    PresenterPagerLifecycle {
  @Override public final void onStart() {

  }

  @Override public void onFragmentShown() {

  }

  @Override public final void onStop() {

  }

  @Override public void onFragmentHidden() {
    getCompositeDisposable().clear();
  }

  @Override public void onFragmentDestroyView() {
    getCompositeDisposable().dispose();
    setView(null);
  }
}
