package com.thumbstage.hydrogen.view.account;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class AccountFragment extends Fragment {

    @BindView(R.id.activity_account_name)
    EditText name;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        name.setText(userViewModel.getCurrentUser().getName());
    }

    @OnClick(R.id.activity_account_signOut)
    public void signOut(View view) {
        userViewModel.signOut();
        ((AccountActivity)getActivity()).onSupportNavigateUp();
    }
}
