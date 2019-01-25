package com.thumbstage.hydrogen.view.browse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thumbstage.hydrogen.view.browse.mine.MineClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.MineOpenedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    public BrowseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new PublishedClosedFragment());
        fragmentList.add(new PublishedOpenedFragment());
        fragmentList.add(new MineClosedFragment());
        fragmentList.add(new MineOpenedFragment());
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
