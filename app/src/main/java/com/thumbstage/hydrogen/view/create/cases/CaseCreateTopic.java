package com.thumbstage.hydrogen.view.create.cases;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.HyFile;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.create.assist.TopicInfoSetupDialog;
import com.thumbstage.hydrogen.view.create.assist.TopicMemberSelectDialog;
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
    Uri imageUri;

    @Override
    public void setSetting(final Fragment fragment) {

        TopicInfoSetupDialog bottomDialog = new TopicInfoSetupDialog();
        bottomDialog.setIOnOK(new TopicInfoSetupDialog.IOnOK() {
            @Override
            public void callback(TopicInfoSetupDialog.LocalData localData) {
                if(!TextUtils.isEmpty(localData.getName())) {
                    topicAdapter.getTopic().setName(localData.getName());
                }
                if(!TextUtils.isEmpty(localData.getBrief())) {
                    topicAdapter.getTopic().setBrief(localData.getBrief());
                }
                imageUri = localData.getImageUri();
                Glide.with(backgroundView).load(imageUri).into(backgroundView);
            }
        });
        bottomDialog.show(fragment.getFragmentManager(), "hello");

    }

    @Override
    public void createTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "createTopic");
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
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
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
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
        bundle.putStringArrayList(RequestResultCode.MemberIds, (ArrayList<String>)DataConvertUtil.user2StringId(topicAdapter.getTopic().getMembers()));
        TopicMemberSelectDialog bottomDialog = new TopicMemberSelectDialog();
        bottomDialog.setArguments(bundle);
        bottomDialog.setIOnOK(new TopicMemberSelectDialog.IOnOK() {
            @Override
            public void callback(final List<User> userList) {
                topicViewModel.updateMembers(userList, new IReturnBool() {
                    @Override
                    public void callback(Boolean isOK) {
                        Log.i(TAG, "addMember ok");
                        topicAdapter.getTopic().setMembers(userList);
                    }
                });
            }
        });
        bottomDialog.show(fragment.getFragmentManager(), "addMember");
    }

    @Override
    public void setUpPopupMenu(PopupWindowAdapter adapter) {
        List<HyMenuItem> itemList = new ArrayList<>();
        itemList.add(new HyMenuItem(R.drawable.ic_menu_setting_b, HyMenuItem.CommandType.SETTING));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_account_plus, HyMenuItem.CommandType.ADD_MEMBER));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_start_g, HyMenuItem.CommandType.START));
        itemList.add(new HyMenuItem(R.drawable.ic_menu_publish_g, HyMenuItem.CommandType.PUBLISH));
        adapter.setItemList(itemList);
    }

}
