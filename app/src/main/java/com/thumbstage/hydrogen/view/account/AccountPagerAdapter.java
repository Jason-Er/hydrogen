package com.thumbstage.hydrogen.view.account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public AccountPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<Fragment>(){{
            add(new AccountFragment());
            add(new ContactFragment());
        }};
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
