package io.cityos.cityosair.ui.base.fragment;

import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.base.presenter.PagerPresenter;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasePagerFragment extends BaseFragment implements ViewPagerFragmentLifecycle {

  private boolean shouldDestroyView;
  private AtomicBoolean fragmentShown = new AtomicBoolean(false);

  @Override
  abstract protected BasePresenter getFragmentPresenter();

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onFragmentShown() {
    fragmentShown.set(true);
    shouldDestroyView = true;
    if (getFragmentPresenter() != null) {
      ((PagerPresenter) getFragmentPresenter()).onFragmentShown();
    }
  }

  @Override
  public void onFragmentHidden() {
    fragmentShown.set(false);
    if (getFragmentPresenter() != null) {
      ((PagerPresenter) getFragmentPresenter()).onFragmentHidden();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (shouldDestroyView) {
      onFragmentDestroyView();
    }
  }

  @Override
  public void onFragmentDestroyView() {
    if (getFragmentPresenter() != null) {
      ((PagerPresenter) getFragmentPresenter()).onFragmentDestroyView();
    }
  }

  boolean isFragmentShown() {
    return fragmentShown.get();
  }
}
