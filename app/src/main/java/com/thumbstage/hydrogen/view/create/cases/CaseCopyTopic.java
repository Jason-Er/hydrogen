package com.thumbstage.hydrogen.view.create.cases;

import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;

import java.util.ArrayList;
import java.util.List;

public class CaseCopyTopic extends CaseBase implements ICanCreateTopic, ICanPopupMenu {

    @Override
    public void createTopic(final IReturnBool iReturnBool) {
        super.copyTopic(iReturnBool);
    }

    @Override
    public void setUpPopupMenu() {
        List<HyMenuItem> itemList = new ArrayList<>();
        /*
        if(topicAdapter.getTopic().getTags() != TopicTag.PICK_UP) {
            itemList.add(startItem);
        }
        */
        popupWindowAdapter.setItemList(itemList);
    }
}
