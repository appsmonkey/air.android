package io.cityos.cityosair.ui.main.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import io.cityos.cityosair.ui.base.fragment.BaseFragment;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

public class CustomFragmentStatePagerAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {
  private List<Function<Void, T>> fragmentCreators = new ArrayList<>();

  public CustomFragmentStatePagerAdapter(FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @Override
  public T getItem(int position) {
    try {
      return fragmentCreators.get(position).apply(null);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public int getCount() {
    return fragmentCreators.size();
  }

  public CustomFragmentStatePagerAdapter setFragmentCreators(List<Function<Void, T>> fragmentCreators) {
    this.fragmentCreators = fragmentCreators;
    return this;
  }
}
