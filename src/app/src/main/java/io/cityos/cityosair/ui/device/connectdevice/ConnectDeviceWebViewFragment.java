package io.cityos.cityosair.ui.device.connectdevice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import io.cityos.cityosair.R;
import io.cityos.cityosair.ui.base.fragment.NewBaseFragment;
import io.cityos.cityosair.ui.device.choosecoordinates.ChooseCoordinatesFragment;
import io.cityos.cityosair.ui.map.BasePresenter;

public class ConnectDeviceWebViewFragment extends NewBaseFragment {

  @BindView(R.id.web_view) WebView webView;

  @SuppressLint("SetJavaScriptEnabled")
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    WebViewClient webViewClient = new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // this is a boxy url used to save settings setup on connection manager provided by boxy
        if (url.equals("http://192.168.4.1/wifisave")) {
          FragmentManager manager = getActivity().getSupportFragmentManager();
          FragmentTransaction transaction = manager.beginTransaction();

          transaction.replace(R.id.a_layout, new ChooseCoordinatesFragment());
          transaction.addToBackStack(null);
          transaction.commit();
        }
      }
    };

    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(webViewClient);
    // load connection manager page provided by boxy once connected to boxy ap
    webView.loadUrl("http://192.168.4.1/");
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override protected int getFragmentLayoutId() {
    return R.layout.fragment_connect_web_view;
  }

  @Override protected BasePresenter getFragmentPresenter() {
    return null;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
