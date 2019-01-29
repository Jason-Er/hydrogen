package com.thumbstage.hydrogen.view.sign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {
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

    }
}
