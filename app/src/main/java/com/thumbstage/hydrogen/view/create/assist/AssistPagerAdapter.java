package com.thumbstage.hydrogen.view.create.assist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssistPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    public AssistPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new TopicInfo());
        fragmentList.add(new TopicMember());
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
