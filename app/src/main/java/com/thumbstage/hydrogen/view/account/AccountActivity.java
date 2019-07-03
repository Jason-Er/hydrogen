package com.thumbstage.hydrogen.view.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class AccountActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public enum Type {
        PROFILE, CONTACT, SELECT_MEMBER
    }

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.account_content)
    ViewPager viewPager;

    AccountPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        configureDagger();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        pagerAdapter = new AccountPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        String type = getIntent().getStringExtra(AccountActivity.Type.class.getSimpleName());
        switch (AccountActivity.Type.valueOf(type)) {
            case PROFILE:
                viewPager.setCurrentItem(0);
                break;
            case CONTACT:
                viewPager.setCurrentItem(1);
                break;
            case SELECT_MEMBER:
                viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }
}
