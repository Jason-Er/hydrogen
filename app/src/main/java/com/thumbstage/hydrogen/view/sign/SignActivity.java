package com.thumbstage.hydrogen.view.sign;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.thumbstage.hydrogen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignActivity extends AppCompatActivity {
    @BindView(R.id.activity_sign)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.sign));

        viewPager.setAdapter(new SignFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void navigate2SignIn() {
        viewPager.setCurrentItem(0);
    }

    public void navigate2SignUp() {
        viewPager.setCurrentItem(1);
    }
}
