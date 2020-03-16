package io.cityos.cityosair.ui.device.devicename;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.cityos.cityosair.app.CityOSAirApplication;
import io.cityos.cityosair.R;
import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.util.cache.CacheUtil;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.map.BasePresenter;
import io.cityos.cityosair.util.app.AndroidUtils;
import javax.inject.Inject;

public class DeviceNameFragment extends NewBaseFragment implements DeviceNameView {
  private AddDevicePayload addDevicePayload;

  private Location gpsLocation;
  private Location networkLocation;
  private Location finalLocation;

  @Inject DeviceNamePresenter deviceNamePresenter;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.btn_add_device) Button btnAddDevice;
  @BindView(R.id.device_name) EditText deviceName;
  @BindView(R.id.switch_indoor) SwitchCompat switchIndoor;

  @BindString(R.string.add_device) String strAddDevice;
  @BindString(R.string.adding_device) String strAddingDevice;

  @OnClick(R.id.btn_add_device) void onButtonClick() {
    if (inputValid(deviceName.getText().toString())) {
      setLoadingView(strAddingDevice);
      addDevice(deviceName.getText().toString());
    }
  }

  private void addDevice(String deviceName) {
    addDevicePayload.setName(deviceName);
    addDevicePayload.setIndoor(switchIndoor.isChecked());
    deviceNamePresenter.addDevice(addDevicePayload);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidUtils.setToolbar((AppCompatActivity) getActivity(), toolbar, strAddDevice, true);

    addDevicePayload = CacheUtil.getSharedCache().getAddDevicePayload();

    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((CityOSAirApplication) getActivity().getApplication()).getMainComponent().inject(this);
  }

  @Override protected int getFragmentLayoutId() {
    return R.layout.fragment_device_name;
  }

  @Override protected BasePresenter getFragmentPresenter() {
    return deviceNamePresenter;
  }

  @Override public void deviceAdded(String token) {
    CacheUtil.getSharedCache().deleteAddDevicePayload();
    Intent intent = new Intent();
    intent.putExtra(Constants.DEVICE_TOKEN, token);
    getActivity().setResult(1, intent);
    getActivity().finish();
  }

  @Override public void addDeviceError() {
    showDialog("No internet connection");
  }

  private boolean inputValid(String name) {
    boolean isValid = true;
    String message = "";
    if (name.isEmpty()) {
      message = "You must fill this field";
      this.deviceName.setError("You must fill this field");
      isValid = false;
    }

    if (!isValid) {
      showDialog(message);
    }

    return isValid;
  }
}
