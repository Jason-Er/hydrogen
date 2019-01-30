package com.thumbstage.hydrogen.view.browse.mine;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IStartedFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    String[] titles = new String[]{"Opened","Closed"};
    public IStartedFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new IStartedOpenedFragment());
        fragmentList.add(new IStartedClosedFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
