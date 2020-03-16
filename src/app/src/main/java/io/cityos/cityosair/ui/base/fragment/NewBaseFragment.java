package io.cityos.cityosair.ui.base.fragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.cityos.cityosair.R;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.map.BaseView;
import io.cityos.cityosair.util.app.AndroidUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NewBaseFragment extends Fragment implements BaseView {

  private Unbinder unbinder;
  @Nullable
  @BindView(R.id.progress_overlay) View progressOverlay;
  @BindString(R.string.default_loading_text) String strLoadingText;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(getFragmentLayoutId(), container, false);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

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

    unbinder.unbind();
    if (getFragmentPresenter() != null) {
      getFragmentPresenter().onDestroyView();
    }
  }

  @Override public void setLoadingView(String message) {
    if (progressOverlay != null) {
      AndroidUtils.showLoadingView(progressOverlay, message != null ? message : strLoadingText);
    }
  }

  @Override public void setContentView() {
    if (progressOverlay != null) {
      AndroidUtils.hideLoadingView(progressOverlay);
    }
  }

  public boolean validateEmail(String email) {
    if (email.isEmpty()) {
      return false;
    } else {
      return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
  }

  public boolean validatePasswordMatching(String password, String repeatPassword) {
    if (password.isEmpty()) {
      return false;
    } else if (!isValidPassword(password)) {
      return false;
    } else if (repeatPassword.isEmpty()) {
      return false;
    } else {
      return password.equals(repeatPassword);
    }
  }

  public boolean isValidPassword(final String password) {

    Pattern pattern;
    Matcher matcher;

    final String PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$";

    pattern = Pattern.compile(PASSWORD_PATTERN);
    matcher = pattern.matcher(password);

    return matcher.matches();
  }

  public void showDialog(String message) {
    new AlertDialog.Builder(getContext())
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {

        })
        .show();
  }

  protected abstract BasePresenter getFragmentPresenter();

  protected abstract int getFragmentLayoutId();
}
