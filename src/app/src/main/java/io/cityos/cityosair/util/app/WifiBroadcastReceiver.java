package io.cityos.cityosair.util.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiBroadcastReceiver extends BroadcastReceiver {

  BroadCastReceiverInterface broadCastReceiverInterface;

  public interface BroadCastReceiverInterface {
    void connectedToBoxy();
  }

  public WifiBroadcastReceiver(
      BroadCastReceiverInterface broadCastReceiverInterface) {
    this.broadCastReceiverInterface = broadCastReceiverInterface;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
      SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
      boolean connected = checkConnectedToDesiredWifi(context);
      if (connected) {
        broadCastReceiverInterface.connectedToBoxy();
      }
    }
  }

  /** Detect you are connected to a specific network. */
  private boolean checkConnectedToDesiredWifi(Context context) {
    boolean connected = false;

    String desiredMacAddress = "Boxy";

    WifiManager wifiManager =
        (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    WifiInfo wifi = wifiManager.getConnectionInfo();
    if (wifi != null) {
      // get current router Mac address
      String bssid = wifi.getSSID();
      connected = bssid.contains(desiredMacAddress);
    }

    return connected;
  }
}
