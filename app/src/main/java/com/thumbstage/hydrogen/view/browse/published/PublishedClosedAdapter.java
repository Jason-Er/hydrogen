package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class PublishedClosedAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_CLOSED
    }

    public PublishedClosedAdapter() {
        delegatesManager.addDelegate(new PublishedClosedAdapterDelegate(view_type.PUBLISHED_CLOSED.ordinal()));
    }

}
