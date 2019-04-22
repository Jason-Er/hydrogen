package com.thumbstage.hydrogen.view.create.assist;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssistPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    String[] titles = new String[]{"TopicInfo","TopicMember"};

    public AssistPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        // fragmentList.add(new TopicInfoSetupDialog());
        // fragmentList.add(new TopicMemberSelectDialog());
    }

    public String[] getTitles() {
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
        return titles[position];
    }
}
