package io.cityos.cityosair.ui.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.map.BaseView;

public abstract class BaseFragment extends Fragment implements BaseView {

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onViewCreated(this, savedInstanceState);
    }
  }

  @Override public void onStart() {
    super.onStart();

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onStart();
    }
  }

  @Override public void onResume() {
    super.onResume();

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onResume();
    }
  }

  @Override public void onPause() {
    super.onPause();

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onPause();
    }
  }

  @Override public void onStop() {
    super.onStop();

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onStop();
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onSaveInstanceState(outState);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onDestroyView();
    }
  }

  protected abstract BasePresenter getFragmentPresenter();
}
