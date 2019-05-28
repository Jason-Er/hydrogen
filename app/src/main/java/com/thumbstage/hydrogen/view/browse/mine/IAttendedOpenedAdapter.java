package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.common.PagedListDelegationAdapter;

public class IAttendedOpenedAdapter extends PagedListDelegationAdapter {

    enum view_type {
        ATTENDED_OPENED
    }

    public IAttendedOpenedAdapter() {
        delegatesManager.addDelegate(new IAttendedOpenedAdapterDelegate(view_type.ATTENDED_OPENED.ordinal()));
    }

}
