package com.thumbstage.hydrogen.view.sign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.SignEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {

    @BindView(R.id.fragment_signUp_name)
    EditText name;
    @BindView(R.id.fragment_signUp_password)
    EditText password;
    @BindView(R.id.fragment_signUp_password2)
    EditText password2;
    @BindView(R.id.fragment_signUp_email)
    EditText email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fragment_signUp_signIn)
    public void signIn(View view) {
        ((SignActivity) getActivity()).navigate2SignIn();
    }

    @OnClick(R.id.fragment_signUp_signUp)
    public void signUp(View view) {
        if( name.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_name), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( password.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( password2.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_consistency_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( !password.getText().toString().equals(password2.getText().toString()) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_consistency_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( email.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_email), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        final AVUser user = new AVUser();
        user.setUsername(name.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if( e == null ) {
                    SignEvent event = new SignEvent(user, "signUser");
                    EventBus.getDefault().post(event);
                    // CurrentUser.getInstance().setAvUser(AVUser.getCurrentUser());
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                } else {
                    switch (e.getCode()) {
                        case 202: {
                            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_name_exist), Toast.LENGTH_SHORT);
                            toast.show();
                            }
                            break;
                        case 203: {
                            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_email_exist), Toast.LENGTH_SHORT);
                            toast.show();
                            }
                            break;
                        case 214: {
                            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_phone_exist), Toast.LENGTH_SHORT);
                            toast.show();
                            }
                            break;
                    }
                }
            }
        });

    }
}
