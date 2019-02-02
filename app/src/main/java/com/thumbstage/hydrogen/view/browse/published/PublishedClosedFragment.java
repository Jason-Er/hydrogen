package com.thumbstage.hydrogen.view.browse.published;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class PublishedClosedFragment extends LCIMConversationListFragment implements BrowseCustomize {

    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.PublishedClosedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
