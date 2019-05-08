package com.thumbstage.hydrogen.view.browse;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedFragment;
import com.thumbstage.hydrogen.view.browse.published.CommunityShowFragment;
import com.thumbstage.hydrogen.view.browse.published.CommunityTopicFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrowseFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Map<String, Fragment> fragmentMap;
    private List<Fragment> fragmentList;

    public BrowseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentMap = new HashMap<String, Fragment>() {
            {
                put(Privilege.BROWSE_COMMUNITY_SHOW.name(), new CommunityShowFragment());
                put(Privilege.BROWSE_COMMUNITY_TOPIC.name(), new CommunityTopicFragment());
                put(Privilege.BROWSE_I_ATTENDED.name(), new IAttendedFragment());
            }
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
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

    public void updateFragmentsBy(@NonNull Set<Privilege> privilegeSet) {
        fragmentList.clear();
        for(Privilege pr : privilegeSet) {
            if(fragmentMap.containsKey(pr.name())) {
                fragmentList.add(fragmentMap.get(pr.name()));
            }
        }
        notifyDataSetChanged();
    }

}
