package com.thumbstage.hydrogen.view.create.cases;

import android.support.v4.app.Fragment;

import com.thumbstage.hydrogen.model.bo.CanOnMic;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CaseCreateTopic extends CaseBase implements ICanPopupMenu, ICanCreateTopic,
        ICanPublishTopic, ICanSetSetting, ICanAddMember {

    final String TAG = "CaseCreateTopic";

    @Override
    public void setSetting(Fragment fragment) {
        super.setSetting(fragment);
    }

    @Override
    public void addMember(Fragment fragment) {
        super.addMember(fragment);
    }

    @Override
    public void createTopic(final IReturnBool iReturnBool) {
        super.createTopic(iReturnBool);
    }

    @Override
    public void publishTopic(final IReturnBool iReturnBool) {
        super.publishTopic(iReturnBool);
    }

    @Override
    public void setUpPopupMenu(Set<CanOnMic> canOnMics) {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(settingItem);
        itemList.add(membersItem);
        switch (topicAdapter.getTopic().getType()) {
            case UNDEFINED:
                itemList.add(startItem);
                itemList.add(publishItem);
                break;
            case PUBLISHED:
                break;
            case UNPUBLISHED:
                itemList.add(publishItem);
                break;
            case PICK_UP:
                break;
        }
        popupWindowAdapter.setItemList(itemList);
    }

}
