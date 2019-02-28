package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class PublishedOpenedAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_OPENED
    }

    public PublishedOpenedAdapter() {
        delegatesManager.addDelegate(new PublishedOpenedAdapterDelegate(view_type.PUBLISHED_OPENED.ordinal()));
    }

}
