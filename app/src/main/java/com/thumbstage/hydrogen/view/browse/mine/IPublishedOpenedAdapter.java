package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class IPublishedOpenedAdapter extends ListDelegationAdapter {

    enum view_type {
        PUBLISHED_OPENED
    }

    public IPublishedOpenedAdapter() {
        delegatesManager.addDelegate(new IPublishedOpenedAdapterDelegate(view_type.PUBLISHED_OPENED.ordinal()));
    }

}
