package com.thumbstage.hydrogen.view.browse.published;

import com.thumbstage.hydrogen.view.browse.ListDelegationAdapter;

public class PublishedOpenedRecyclerAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_OPENED
    }

    public PublishedOpenedRecyclerAdapter() {
        delegatesManager.addDelegate(new PublishedOpenedAdapterDelegate(view_type.PUBLISHED_OPENED.ordinal()));
    }

}
