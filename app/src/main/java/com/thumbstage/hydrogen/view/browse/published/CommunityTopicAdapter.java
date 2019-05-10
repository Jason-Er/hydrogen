package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class CommunityTopicAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_OPENED
    }

    public CommunityTopicAdapter() {
        delegatesManager.addDelegate(new CommunityTopicAdapterDelegate(view_type.PUBLISHED_OPENED.ordinal()));
    }

}
