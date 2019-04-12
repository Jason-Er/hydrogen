package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class IStartedClosedAdapter extends ListDelegationAdapter {

    enum view_type {
        STARTED_CLOSED
    }

    public IStartedClosedAdapter() {
        delegatesManager.addDelegate(new IStartedClosedAdapterDelegate(view_type.STARTED_CLOSED.ordinal()));
    }

}
