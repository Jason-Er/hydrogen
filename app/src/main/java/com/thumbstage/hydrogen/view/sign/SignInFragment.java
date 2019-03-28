package com.thumbstage.hydrogen.view.sign;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.viewmodel.SignViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class SignInFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.fragment_signIn_name)
    EditText name;
    @BindView(R.id.fragment_signIn_password)
    EditText password;

    SignViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureDagger();
        configureViewModel();
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignViewModel.class);
    }

    @OnClick(R.id.fragment_signIn_signIn)
    public void signIn(View view) {

        String name_ = name.getText().toString();
        String password_ = password.getText().toString();

        if( TextUtils.isEmpty(name_) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_name), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( TextUtils.isEmpty(password_) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        viewModel.signIn(name_, password_).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if( user != null ) {
                    Intent intent = new Intent();
                    intent.putExtra(User.class.getSimpleName(), user);
                    getActivity().setResult(2, intent);
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                } else {
                    Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.wrong_name_password), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        /*
        AVUser.logInInBackground(name.getText().toString(), password.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if( e == null ) {
                    SignEvent event = new SignEvent(user, "signUser");
                    EventBus.getDefault().post(event);
                    CurrentUser.getInstance().setAvUser(user);
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                } else {
                    Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.wrong_name_password), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        */
    }

    @OnClick(R.id.fragment_signIn_signUp)
    public void signUp(View view) {
        ((SignActivity) getActivity()).navigate2SignUp();
    }
}
