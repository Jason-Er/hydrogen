package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.PagedListDelegationAdapter;

public class IAttendedClosedAdapter extends PagedListDelegationAdapter {

    enum view_type {
        ATTENDED_CLOSED
    }

    public IAttendedClosedAdapter() {
        delegatesManager.addDelegate(new IAttendedClosedAdapterDelegate(view_type.ATTENDED_CLOSED.ordinal()));
    }

}
