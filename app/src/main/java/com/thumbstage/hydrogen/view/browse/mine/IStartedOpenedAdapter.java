package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class IStartedOpenedAdapter extends ListDelegationAdapter {

    enum view_type {
        STARTED_OPENED
    }

    public IStartedOpenedAdapter() {
        delegatesManager.addDelegate(new IStartedOpenedAdapterDelegate(view_type.STARTED_OPENED.ordinal()));
    }

}
