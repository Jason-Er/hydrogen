package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class CommunityShowAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_CLOSED
    }

    public CommunityShowAdapter() {
        delegatesManager.addDelegate(new CommunityShowAdapterDelegate(view_type.PUBLISHED_CLOSED.ordinal()));
    }

}
