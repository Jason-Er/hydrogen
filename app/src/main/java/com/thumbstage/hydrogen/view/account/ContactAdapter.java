package com.thumbstage.hydrogen.view.account;

import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.PinyinUtils;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class ContactAdapter extends ListDelegationAdapter {

    public enum ModelType {
        LIST_CONTACT, SELECT_CONTACT
    }

    public ContactAdapter() {
        delegatesManager.addDelegate(new ListContactUserDelegate(ModelType.LIST_CONTACT.ordinal()));
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = PinyinUtils.getFirstCharacter(((User)getItems().get(i)).getName());
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public void setAdapterModel(ModelType type) {
        switch (type) {
            case LIST_CONTACT:
                delegatesManager.removeAllDelegate().addDelegate(new ListContactUserDelegate(type.ordinal()) );
                break;
            case SELECT_CONTACT:
                delegatesManager.removeAllDelegate().addDelegate(new SelectContactUserDelegate(type.ordinal()) );
                break;
        }
    }

}
