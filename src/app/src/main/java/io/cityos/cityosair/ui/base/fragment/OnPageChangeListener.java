package io.cityos.cityosair.ui.base.fragment;

import androidx.viewpager.widget.ViewPager;

import io.cityos.cityosair.ui.main.adapter.CustomFragmentStatePagerAdapter;

public class OnPageChangeListener implements ViewPager.OnPageChangeListener {
  private CustomFragmentStatePagerAdapter pageAdapter;
  private int currentPosition;
  private ViewPager viewPager;

  public OnPageChangeListener(CustomFragmentStatePagerAdapter pageAdapter,
      final ViewPager viewPager, final int currentPosition) {
    this.viewPager = viewPager;
    this.pageAdapter = pageAdapter;
    this.currentPosition = currentPosition;
    if (this.viewPager != null) {
      this.viewPager.post(() -> {
        viewPager.setCurrentItem(currentPosition);
        onPageSelected(currentPosition);
      });
    }
  }

  public OnPageChangeListener(CustomFragmentStatePagerAdapter pageAdapter, final ViewPager viewPager) {
    this(pageAdapter, viewPager, 0);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override public void onPageSelected(int newPosition) {
    BasePagerFragment fragmentToShow =
        (BasePagerFragment) pageAdapter.instantiateItem(viewPager, newPosition);
    fragmentToShow.onFragmentShown();

    if (currentPosition != newPosition) {
      BasePagerFragment fragmentToHide =
          (BasePagerFragment) pageAdapter.instantiateItem(viewPager, currentPosition);
      fragmentToHide.onFragmentHidden();
    }

    currentPosition = newPosition;
  }

  @Override public void onPageScrollStateChanged(int state) {

  }
}

