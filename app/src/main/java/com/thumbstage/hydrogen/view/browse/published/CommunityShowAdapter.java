package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.PagedListDelegationAdapter;

public class CommunityShowAdapter extends PagedListDelegationAdapter {

    enum view_type {
        PUBLISHED_CLOSED
    }

    public CommunityShowAdapter() {
        delegatesManager.addDelegate(new CommunityShowAdapterDelegate(view_type.PUBLISHED_CLOSED.ordinal()));
    }

}
