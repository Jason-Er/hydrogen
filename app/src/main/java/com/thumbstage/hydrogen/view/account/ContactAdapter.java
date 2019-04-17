package com.thumbstage.hydrogen.view.account;

import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class ContactAdapter extends ListDelegationAdapter {

    enum view_type {
        CONTACT_USER
    }

    public ContactAdapter() {
        delegatesManager.addDelegate(new ContactUserDelegate(view_type.CONTACT_USER.ordinal()));
    }

    /*
    public int getSectionForPosition(int position) {
        return getItems().get(position).getLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
    */
}
