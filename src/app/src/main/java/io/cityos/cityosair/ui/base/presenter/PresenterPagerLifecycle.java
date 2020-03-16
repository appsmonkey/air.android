package io.cityos.cityosair.ui.base.presenter;

public interface PresenterPagerLifecycle {
  void onFragmentShown();

  void onFragmentHidden();

  void onFragmentDestroyView();
}
