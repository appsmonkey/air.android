package io.cityos.cityosair.ui.base.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseTabFragment extends NewBaseFragment implements BaseTabView {
  private OnPageChangeListener onPageChangeListenerWithLifecycle;
  private boolean skipOnStarted;
  private int startingItem = 0;

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
  }

  @Override public final void onStart() {
    super.onStart();
    onStarting();
    if (!skipOnStarted) onStarted();
  }

  @Override public void onStop() {
    super.onStop();
    if (getCurrentPagerFragment() != null && getCurrentPagerFragment().isFragmentShown()) {
      getCurrentPagerFragment().onFragmentHidden();
    }
    getViewPager().removeOnPageChangeListener(onPageChangeListenerWithLifecycle);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    onPageChangeListenerWithLifecycle = null;
  }

  public final BasePagerFragment getCurrentPagerFragment() {
    if (getViewPager() == null || getPagerAdapter() == null) return null;

    return (BasePagerFragment) getPagerAdapter().instantiateItem(getViewPager(),
        getViewPager().getCurrentItem());
  }

  abstract public void onStarting();

  public int getStartingItem() {
    return startingItem;
  }

  public void setStartingItem(int startingItem) {
    this.startingItem = startingItem;
  }

  public void setSkipOnStarted(boolean skipOnStarted) {
    this.skipOnStarted = skipOnStarted;
  }

  protected void onStarted() {
    onPageChangeListenerWithLifecycle =
        onPageChangeListenerWithLifecycle != null ? getOnPageChangeListenerWithLifecycle(
            getViewPager().getCurrentItem())
            : getOnPageChangeListenerWithLifecycle(getStartingItem());
    if (onPageChangeListenerWithLifecycle != null) {
      getViewPager().addOnPageChangeListener(onPageChangeListenerWithLifecycle);
    }
  }
}
