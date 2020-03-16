package io.cityos.cityosair.ui.map;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.cityos.cityosair.ui.base.presenter.PresenterLifecycle;
import io.reactivex.disposables.CompositeDisposable;
import javax.annotation.Nonnull;

public class BasePresenter<T extends BaseView> implements PresenterLifecycle {

  private BaseView view;
  private Context context;
  private CompositeDisposable compositeDisposable;

  public BasePresenter() {
  }

  public void onViewCreated(@NonNull T view, @Nullable Bundle savedInstanceState) {
    compositeDisposable = new CompositeDisposable();
    this.view = view;
  }


  public BasePresenter<T> setView(BaseView view) {
    this.view = view;
    return this;
  }

  public T getView() {
    return (T) view;
  }

  public CompositeDisposable getCompositeDisposable() {
    return compositeDisposable;
  }

  @Override
  public void onStart() {

  }

  public void onActivityStart(@Nonnull T view) {
    compositeDisposable = new CompositeDisposable();
    this.view = view;
  }

  @Override
  public void onResume() {

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onStop() {
    compositeDisposable.clear();
  }

  @Override
  public void onDestroyView() {
    compositeDisposable.dispose();
  }
}
