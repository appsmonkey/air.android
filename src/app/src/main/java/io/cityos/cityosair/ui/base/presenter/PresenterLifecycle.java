package io.cityos.cityosair.ui.base.presenter;

import android.os.Bundle;

public interface PresenterLifecycle {
  void onStart();

  void onResume();

  void onSaveInstanceState(Bundle outState);

  void onPause();

  void onStop();

  void onDestroyView();
}
