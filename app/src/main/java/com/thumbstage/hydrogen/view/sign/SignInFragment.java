package com.thumbstage.hydrogen.view.sign;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class SignInFragment extends Fragment {

    @BindView(R.id.fragment_signIn_name)
    EditText name;
    @BindView(R.id.fragment_signIn_password)
    EditText password;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
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

        spinner.setVisibility(View.VISIBLE);
        viewModel.signIn(name_, password_).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if( !user.getId().equals(StringUtil.DEFAULT_USERID) ) {
                    spinner.setVisibility(View.GONE);
                    ((SignActivity) getActivity()).onSupportNavigateUp();
                }
            }
        });
    }

    @OnClick(R.id.fragment_signIn_signUp)
    public void signUp(View view) {
        ((SignActivity) getActivity()).navigate2SignUp();
    }

    @OnClick(R.id.fragment_signIn_showPwd)
    public void showPassword(View view) {
        if(view.getTag() == null || !(Boolean) view.getTag()) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password.setSelection(password.getText().length());
            ((ImageButton)view).setImageResource(R.drawable.ic_button_eyeon);
            view.setTag(true);
        } else {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setSelection(password.getText().length());
            ((ImageButton)view).setImageResource(R.drawable.ic_button_eyeoff);
            view.setTag(false);
        }
    }
}
