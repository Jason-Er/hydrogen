package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class IPublishedClosedAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_CLOSED
    }

    public IPublishedClosedAdapter() {
        delegatesManager.addDelegate(new IPublishedClosedAdapterDelegate(view_type.PUBLISHED_CLOSED.ordinal()));
    }

}
