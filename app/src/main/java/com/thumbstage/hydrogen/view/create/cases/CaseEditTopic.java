package com.thumbstage.hydrogen.view.create.cases;

import android.support.v4.app.Fragment;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;

import java.util.ArrayList;
import java.util.List;

public class CaseEditTopic extends CaseBase implements ICanPopupMenu,
        ICanSetSetting, ICanPublishTopic, ICanCloseTopic {

    @Override
    public void closeTopic(IReturnBool iReturnBool) {
        topicViewModel.closeTheTopic(iReturnBool);
    }

    @Override
    public void publishTopic(IReturnBool iReturnBool) {

    }

    @Override
    public void setSetting(Fragment fragment) {

    }

    @Override
    public void setUpPopupMenu(PopupWindowAdapter adapter) {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(new HyMenuItem(R.drawable.ic_menu_setting_b, HyMenuItem.CommandType.SETTING));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_publish_g, HyMenuItem.CommandType.PUBLISH));
        adapter.setItemList(itemList);
    }
}
