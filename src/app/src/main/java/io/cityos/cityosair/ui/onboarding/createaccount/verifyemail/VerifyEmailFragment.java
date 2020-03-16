package io.cityos.cityosair.ui.onboarding.createaccount.verifyemail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.cityos.cityosair.R;

/*
  Show user fragment that a verification email has been sent to his email account
 */
public class VerifyEmailFragment extends Fragment {

  @BindView(R.id.verify_email_text_view)
  EditText verifyEmailTextView;
  private String signUpEmail;
  public static String EMAIL = "email";

  public VerifyEmailFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_verify_email, container, false);

    ButterKnife.bind(this, view);

    signUpEmail = getArguments().getString(EMAIL, "");

    String verifyEmailString = getString(R.string.verify_email_screen_email_sent_message_text_view, signUpEmail);
    verifyEmailTextView.setText(verifyEmailString);
    verifyEmailTextView.setKeyListener(null);

    return view;
  }

}
