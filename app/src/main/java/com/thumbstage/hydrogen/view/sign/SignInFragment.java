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
import com.avos.avoscloud.LogInCallback;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.common.SignEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends Fragment {

    @BindView(R.id.fragment_signIn_name)
    EditText name;
    @BindView(R.id.fragment_signIn_password)
    EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fragment_signIn_signIn)
    public void signIn(View view) {

        if( name.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_name), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( password.getText().toString().isEmpty() ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        AVUser.logInInBackground(name.getText().toString(), password.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if( e == null ) {
                    SignEvent event = new SignEvent(user, "signUser");
                    EventBus.getDefault().post(event);
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                } else {
                    Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.wrong_name_password), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @OnClick(R.id.fragment_signIn_signUp)
    public void signUp(View view) {
        ((SignActivity) getActivity()).navigate2SignUp();
    }
}
