package io.cityos.cityosair.util.app;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.ui.onboarding.changepassword.ChangePasswordFragment;
import io.cityos.cityosair.ui.onboarding.createaccount.signup.SignUpFragment;
import io.cityos.cityosair.ui.onboarding.resetpassword.ResetPasswordFragment;

public class DeepLinksHandler {
  public static void handleDeepLinks(FragmentManager fragmentManager, Uri data) {
    String clientId = data.getQueryParameter("client_id");
    String userName = data.getQueryParameter("user_name");
    String confirmationCode = data.getQueryParameter("confirmation_code");
    String type = data.getQueryParameter("type");
    String cogId = data.getQueryParameter("cog_id");

    // if deep link is registration type handle registration flow
    if (type.equals(Constants.VERIY_EMAIL_REGISTRATION_TYPE)) {
      Bundle bundle = new Bundle();
      VerifyEmailPayload verifyEmailPayload = new VerifyEmailPayload(clientId, userName, confirmationCode, type, cogId);
      bundle.putParcelable(SignUpFragment.VERIFY_EMAIL_PAYLOAD, verifyEmailPayload);
      SignUpFragment signUpFragment = new SignUpFragment();
      signUpFragment.setArguments(bundle);

      fragmentManager.beginTransaction()
          .add(android.R.id.content, signUpFragment)
          .commit();
    }
    // if deep link is reset password type handle reset password flow
    else if (type.equals(Constants.VERIY_EMAIL_PASSWORD_TYPE)) {
      Bundle bundle = new Bundle();
      VerifyEmailPayload verifyEmailPayload = new VerifyEmailPayload(clientId, userName, confirmationCode, type, cogId);
      bundle.putParcelable(ChangePasswordFragment.VERIFY_EMAIL_PAYLOAD, verifyEmailPayload);
      ChangePasswordFragment resetPasswordFragment = new ChangePasswordFragment();
      resetPasswordFragment.setArguments(bundle);

      fragmentManager.beginTransaction()
          .add(android.R.id.content, resetPasswordFragment)
          .commit();
    }
  }
}
