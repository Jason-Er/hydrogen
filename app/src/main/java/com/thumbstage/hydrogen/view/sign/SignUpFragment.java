package com.thumbstage.hydrogen.view.sign;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class SignUpFragment extends Fragment {

    @BindView(R.id.fragment_signUp_name)
    EditText name;
    @BindView(R.id.fragment_signUp_password)
    EditText password;
    @BindView(R.id.fragment_signUp_password2)
    EditText password2;
    @BindView(R.id.fragment_signUp_email)
    EditText email;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @OnClick(R.id.fragment_signUp_signIn)
    public void signIn(View view) {
        ((SignActivity) getActivity()).navigate2SignIn();
    }

    @OnClick(R.id.fragment_signUp_signUp)
    public void signUp(View view) {

        String namel = name.getText().toString();
        String passwordl = password.getText().toString();
        String password2l = password2.getText().toString();
        String emaill = email.getText().toString();

        if( TextUtils.isEmpty(namel) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_name), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( TextUtils.isEmpty(passwordl) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( TextUtils.isEmpty(password2l) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_consistency_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( ! passwordl.equals(password2l) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_consistency_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if( TextUtils.isEmpty(emaill) ) {
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.attention_empty_email), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        spinner.setVisibility(View.VISIBLE);
        viewModel.signUp(namel, passwordl, emaill).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(!user.getId().equals(StringUtil.DEFAULT_USERID)) {
                    spinner.setVisibility(View.GONE);
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                }
            }
        });

    }
}
