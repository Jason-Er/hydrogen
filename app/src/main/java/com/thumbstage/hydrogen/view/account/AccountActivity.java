package com.thumbstage.hydrogen.view.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVUser;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.activity_account_name)
    EditText name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.profile));

        AVUser currentUser = AVUser.getCurrentUser();

        name.setText(currentUser.getUsername());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.activity_account_signOut)
    public void signOut(View view) {
        AVUser.logOut();
        UserManager.getInstance().setAvUser(AVUser.getCurrentUser());
        onSupportNavigateUp();
    }

}
