package io.cityos.cityosair.ui.base.fragment;

import androidx.viewpager.widget.ViewPager;

import io.cityos.cityosair.ui.main.adapter.CustomFragmentStatePagerAdapter;

public interface BaseTabView {
  BasePagerFragment getCurrentPagerFragment();

  CustomFragmentStatePagerAdapter getPagerAdapter();

  ViewPager getViewPager();

  OnPageChangeListener getOnPageChangeListenerWithLifecycle(int currentPosition);

  int getStartingItem();
}
