package com.thumbstage.hydrogen.view.sign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SignFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    public SignFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new SignInFragment());
        fragmentList.add(new SignUpFragment());
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
