package com.thumbstage.hydrogen.view.browse.mine;

import com.thumbstage.hydrogen.view.browse.ListDelegationAdapter;

public class IAttendedOpenedAdapter extends ListDelegationAdapter {

    enum view_type {
        ATTENDED_OPENED
    }

    public IAttendedOpenedAdapter() {
        delegatesManager.addDelegate(new IAttendedOpenedAdapterDelegate(view_type.ATTENDED_OPENED.ordinal()));
    }

}
