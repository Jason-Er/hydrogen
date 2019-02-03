package com.thumbstage.hydrogen.view.browse.mine;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.view.browse.BrowseCustomize;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class IStartedClosedFragment extends LCIMConversationListFragment implements BrowseCustomize {
    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
