package com.thumbstage.hydrogen.view.create.cases;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.thumbstage.hydrogen.model.bo.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;

import java.util.ArrayList;
import java.util.List;

public class CaseEditTopic extends CaseBase implements ICanPopupMenu,
        ICanSetSetting, ICanAddMember, ICanPublishTopic, ICanCloseTopic {

    @Override
    public void closeTopic(IReturnBool iReturnBool) {
        topicViewModel.closeTheTopic(iReturnBool);
    }

    @Override
    public void publishTopic(IReturnBool iReturnBool) {
        super.publishTopic(iReturnBool);
    }

    @Override
    public void setSetting(Fragment fragment) {
        super.setSetting(fragment);
    }

    @Override
    public void addMember(@NonNull Fragment fragment) {
        super.addMember(fragment);
    }

    @Override
    public void setUpPopupMenu() {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(settingItem);
        itemList.add(membersItem);
        if(topicAdapter.getTopic().getType() == TopicType.UNPUBLISHED) {
            itemList.add(publishItem);
        }
        if(!topicAdapter.getTopic().isFinished()) {
            itemList.add(closeItem);
        }
        popupWindowAdapter.setItemList(itemList);
    }

}
