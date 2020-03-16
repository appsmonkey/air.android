package io.cityos.cityosair.ui.onboarding.entrypoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.urbanairship.UAirship;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.main.MainActivity;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.ui.notifications.NotificationsEnum;
import io.cityos.cityosair.util.app.AndroidUtils;
import io.cityos.cityosair.util.cache.SharedPreferencesManager;

public class EntryFragment extends NewBaseFragment implements EntryView {

  @Inject EntryPresenter entryPresenter;
  @Inject SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.progress_overlay) View progressOverlay;

  @BindString(R.string.login_loading_text) String strLoginLoadingText;

  @OnClick(R.id.btn_login) public void loginClicked() {
    LoginActivity.show(getActivity());
  }

  @OnClick(R.id.btn_sarajevo_air) public void sarajevoAirClicked() {
    entryPresenter.getGuestDevicesAndSchema();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
  }

  @Override protected int getFragmentLayoutId() {
    return R.layout.fragment_entry;
  }

  @Override protected BasePresenter getFragmentPresenter() {
    return entryPresenter;
  }

  @Override public void setLoadingView() {
    AndroidUtils.showLoadingView(progressOverlay, strLoginLoadingText);
  }

  @Override public void setContentView() {
    AndroidUtils.hideLoadingView(progressOverlay);
  }

  @Override public void loginSuccessful(User user) {
    Set tags;
    tags = UAirship.shared().getPushManager().getTags();
    tags.clear();
    tags.add(NotificationsEnum.LAST_GOOD.getTag());
    tags.add(NotificationsEnum.SETTING_SENSITIVE.getTag());

    // this is a guest user
    if (user == null) {
      tags.add(SharedPreferencesManager.REGISTERED_FALSE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED,
          SharedPreferencesManager.REGISTERED_FALSE);
    } else {
      tags.add(SharedPreferencesManager.REGISTERED_TRUE);
      sharedPreferencesManager.set(SharedPreferencesManager.REGISTERED,
          SharedPreferencesManager.REGISTERED_TRUE);
    }

    sharedPreferencesManager.set(SharedPreferencesManager.LAST_NOTIFICATION,
        NotificationsEnum.LAST_GOOD.getTag());
    sharedPreferencesManager.set(SharedPreferencesManager.SETTINGS_NOTIFICATION,
        NotificationsEnum.SETTING_SENSITIVE.getTag());

    UAirship.shared().getPushManager().setTags(tags);
    UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    UAirship.shared().getPushManager().setPushEnabled(true);

    navigateToMainActivity();
  }

  private void navigateToMainActivity() {
    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    getActivity().finish();
  }
}
