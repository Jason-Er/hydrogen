package com.thumbstage.hydrogen.view.create.cases;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;

import java.util.ArrayList;
import java.util.List;

public class CaseCopyTopic extends CaseBase implements ICanCreateTopic, ICanPopupMenu {

    @Override
    public void createTopic(IReturnBool iReturnBool) {
        topicViewModel.createTheTopic(TopicType.PICK_UP, iReturnBool);
    }

    @Override
    public void setUpPopupMenu(PopupWindowAdapter adapter) {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(new HyMenuItem(R.drawable.ic_menu_start_g, HyMenuItem.CommandType.START));
        adapter.setItemList(itemList);
    }
}
