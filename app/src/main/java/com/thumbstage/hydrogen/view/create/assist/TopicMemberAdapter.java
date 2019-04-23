package com.thumbstage.hydrogen.view.create.assist;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

import java.util.List;

public class TopicMemberAdapter extends ListDelegationAdapter {

    enum view_type {
        ADDED, PLUS
    }

    public TopicMemberAdapter() {
        delegatesManager.addDelegate(new TopicMemberAddedDelegate(view_type.ADDED.ordinal()));
        delegatesManager.addDelegate(new TopicMemberPlusDelegate(view_type.PLUS.ordinal()));
    }

    public void setUsers(List items) {
        items.add(new ItemPlus());
        super.setItems(items);
    }

}
