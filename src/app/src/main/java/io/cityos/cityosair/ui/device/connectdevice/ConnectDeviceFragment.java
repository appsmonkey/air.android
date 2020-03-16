package io.cityos.cityosair.ui.device.connectdevice;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.ui.device.choosecoordinates.ChooseCoordinatesFragment;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;

import javax.inject.Inject;

/**
 * First screen fragment in add device workflow
 */
public class ConnectDeviceFragment extends NewBaseFragment implements ConnectDeviceView {

  @Inject ConnectDevicePresenter connectDevicePresenter;

  @BindView(R.id.progress_overlay) View progressOverlay;
  @BindView(R.id.button_configure) Button configureButton;

  @BindString(R.string.scanning_for_device) String strScanningForDevices;
  @BindString(R.string.connecting) String strConnecting;

  @OnClick(R.id.button_settings)
  void openSettings() {
    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), Constants.SETTINGS_REQUEST_CODE);
  }

  @OnClick(R.id.button_configure)
  void openConfigureDevice() {
    connectDevicePresenter.getDeviceId();
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);

    connectDevicePresenter.setView(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    configureButton.setEnabled(false);
  }

  @Override
  protected int getFragmentLayoutId() {
    return R.layout.fragment_connect_device;
  }

  @Override
  protected BasePresenter getFragmentPresenter() {
    return connectDevicePresenter;
  }

  @Override
  public void deviceIdFetched(String deviceId) {
    AddDevicePayload addDevicePayload = new AddDevicePayload();
    addDevicePayload.setToken(deviceId);

    CacheUtil.getSharedCache().save(addDevicePayload);

    FragmentManager manager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    transaction.replace(R.id.a_layout, new ConnectDeviceWebViewFragment());
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // once we're done with connecting to boxy in settings we must use hardware or software back button for this to pass
    // and the next step button to be enabled.
    if (requestCode == Constants.SETTINGS_REQUEST_CODE) {
      configureButton.setEnabled(true);
    }
  }
}
