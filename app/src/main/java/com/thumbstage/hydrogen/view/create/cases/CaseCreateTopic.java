package com.thumbstage.hydrogen.view.create.cases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.bo.HyFile;
import com.thumbstage.hydrogen.model.bo.Setting;
import com.thumbstage.hydrogen.model.bo.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.create.assist.AssistDialogFragment;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CaseCreateTopic extends CaseBase implements ICanPopupMenu, ICanCreateTopic,
        ICanPublishTopic, ICanSetSetting, ICanAddMember {

    final String TAG = "CaseCreateTopic";

    @Override
    public void setSetting(final Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.INFO.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "hello");
    }

    @Override
    public void createTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "createTopic");
        if(topicAdapter.getTopic().getSetting() != null) {
            File file = new File(topicAdapter.getTopic().getSetting().getUrl());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iReturnBool);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iReturnBool);
        }
    }

    @Override
    public void publishTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "publishTopic");
        if(topicAdapter.getTopic().getSetting() != null) {
            File file = new File(topicAdapter.getTopic().getSetting().getUrl());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.PUBLISHED, iReturnBool);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.PUBLISHED, iReturnBool);
        }
    }

    @Override
    public void addMember(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.MEMBER.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "hello");
    }

    @Override
    public void setUpPopupMenu(PopupWindowAdapter adapter) {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(new HyMenuItem(R.drawable.ic_menu_setting_g, HyMenuItem.CommandType.SETTING));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_account_plus, HyMenuItem.CommandType.ADD_MEMBER));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_start_g, HyMenuItem.CommandType.START));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_publish_g, HyMenuItem.CommandType.PUBLISH));
        adapter.setItemList(itemList);
    }

}
