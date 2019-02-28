package com.thumbstage.hydrogen.view.browse.atme;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class AtMeAdapter extends ListDelegationAdapter {
    enum view_type {
        AT_ME
    }

    public AtMeAdapter() {
        delegatesManager.addDelegate(new AtMeAdapterDelegate(view_type.AT_ME.ordinal()));
    }
}
