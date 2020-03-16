package io.cityos.cityosair.ui.base.fragment;

public interface ViewPagerFragmentLifecycle {
  void onFragmentShown();

  void onFragmentHidden();

  void onFragmentDestroyView();
}
