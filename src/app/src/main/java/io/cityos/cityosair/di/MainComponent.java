package io.cityos.cityosair.di;

import dagger.Component;
import io.cityos.cityosair.ui.device.connectdevice.ConnectDeviceFragment;
import io.cityos.cityosair.ui.device.devicename.DeviceNameFragment;
import io.cityos.cityosair.ui.graph.GraphFragment;
import io.cityos.cityosair.ui.onboarding.changepassword.ChangePasswordFragment;
import io.cityos.cityosair.ui.onboarding.entrypoint.EntryFragment;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginActivity;
import io.cityos.cityosair.ui.onboarding.loginfragment.LoginFragment;
import io.cityos.cityosair.ui.onboarding.resetpassword.ResetPasswordFragment;
import io.cityos.cityosair.ui.main.MainActivity;
import io.cityos.cityosair.ui.main.MainFragment;
import io.cityos.cityosair.ui.main.MainTabFragment;
import io.cityos.cityosair.ui.main.SplashActivity;
import io.cityos.cityosair.ui.map.MapFragment;
import io.cityos.cityosair.ui.onboarding.createaccount.registeremail.RegisterEmailFragment;
import io.cityos.cityosair.ui.onboarding.createaccount.signup.SignUpFragment;
import io.cityos.cityosair.ui.settings.SettingsActivity;
import io.cityos.cityosair.ui.notifications.NotificationActivity;
import javax.inject.Singleton;

@Singleton
@Component(modules = { MainModule.class })
public interface MainComponent {
  void inject(MapFragment mapFragment);

  void inject(LoginActivity loginActivity);

  void inject(LoginFragment loginFragment);

  void inject(SplashActivity splashActivity);

  void inject(SettingsActivity settingsActivity);

  void inject(MainActivity mainActivity);

  void inject(MainFragment mainFragment);

  void inject(GraphFragment graphFragment);

  void inject(MainTabFragment mainTabFragment);

  void inject(NotificationActivity notificationActivity);

  void inject(EntryFragment entryFragment);

  void inject(ResetPasswordFragment resetPasswordFragment);

  void inject(SignUpFragment signUpFragment);

  void inject(RegisterEmailFragment registerEmailFragment);

  void inject(ChangePasswordFragment changePasswordFragment);

  void inject(ConnectDeviceFragment connectDeviceFragment);

  void inject(DeviceNameFragment deviceNameFragment);
}
