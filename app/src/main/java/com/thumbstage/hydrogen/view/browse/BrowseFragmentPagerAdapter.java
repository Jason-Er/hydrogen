package com.thumbstage.hydrogen.view.browse;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thumbstage.hydrogen.view.browse.mine.IAttendedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = null;

    public BrowseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof Fragment) {
            if (fragmentList.contains(object)) {
                return fragmentList.indexOf(object);
            } else {
                return POSITION_NONE;
            }
        }
        return super.getItemPosition(object);
    }

    @Override
    public long getItemId(int position) {
        return ((IAdapterFunction)fragmentList.get(position)).getItemId();
    }
}
