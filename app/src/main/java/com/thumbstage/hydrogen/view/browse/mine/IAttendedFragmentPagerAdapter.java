package com.thumbstage.hydrogen.view.browse.mine;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thumbstage.hydrogen.R;

import java.util.ArrayList;
import java.util.List;

public class IAttendedFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    List<String> titles = new ArrayList<>();
    public IAttendedFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles.add(context.getResources().getString(R.string.IAttended_open));
        titles.add(context.getResources().getString(R.string.IAttended_closed));
        fragmentList=new ArrayList<>();
        fragmentList.add(new IAttendedOpenedFragment());
        fragmentList.add(new IAttendedClosedFragment());
    }

    public List<String> getTitles() {
        return titles;
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
        return titles.get(position);
    }
}
