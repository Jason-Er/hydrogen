package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.PagedListDelegationAdapter;

public class CommunityTopicAdapter extends PagedListDelegationAdapter {

    enum view_type {
        PUBLISHED_OPENED
    }

    public CommunityTopicAdapter() {
        delegatesManager.addDelegate(new CommunityTopicAdapterDelegate(view_type.PUBLISHED_OPENED.ordinal()));
    }

}
