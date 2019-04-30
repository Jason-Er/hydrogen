package com.thumbstage.hydrogen.view.create.cases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.model.bo.HyFile;
import com.thumbstage.hydrogen.model.bo.Line;
import com.thumbstage.hydrogen.model.bo.Mic;
import com.thumbstage.hydrogen.model.bo.Setting;
import com.thumbstage.hydrogen.model.bo.TopicType;
import com.thumbstage.hydrogen.model.bo.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.create.assist.AssistDialogFragment;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import java.io.File;
import java.util.List;

public abstract class CaseBase implements ITopicFragmentFunction {

    final String TAG = "CaseBase";

    TopicAdapter topicAdapter;
    LinearLayoutManager layoutManager;
    ImageView backgroundView;
    TopicViewModel topicViewModel;
    RecyclerView recyclerView;
    User user;
    PopupWindowAdapter popupWindowAdapter;

    HyMenuItem settingItem = new HyMenuItem(R.drawable.ic_menu_setting_g, HyMenuItem.CommandType.SETTING);
    HyMenuItem membersItem = new HyMenuItem(R.drawable.ic_menu_account_plus, HyMenuItem.CommandType.MEMBERS);
    HyMenuItem startItem = new HyMenuItem(R.drawable.ic_menu_start_g, HyMenuItem.CommandType.START);
    HyMenuItem publishItem = new HyMenuItem(R.drawable.ic_menu_publish_g, HyMenuItem.CommandType.PUBLISH);
    HyMenuItem closeItem = new HyMenuItem(R.drawable.ic_menu_transcribe_close_g, HyMenuItem.CommandType.CLOSE);


    public CaseBase setTopicAdapter(TopicAdapter topicAdapter) {
        this.topicAdapter = topicAdapter;
        return this;
    }

    public CaseBase setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public CaseBase setBackgroundView(ImageView backgroundView) {
        this.backgroundView = backgroundView;
        return this;
    }

    public CaseBase setTopicViewModel(TopicViewModel topicViewModel) {
        this.topicViewModel = topicViewModel;
        return this;
    }

    public CaseBase setPopupWindowAdapter(PopupWindowAdapter popupWindowAdapter) {
        this.popupWindowAdapter = popupWindowAdapter;
        return this;
    }

    public CaseBase setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void handleBottomBarEvent(TopicBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    void setSetting(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.INFO.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "CaseBase");
    }

    void addMember(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.MEMBER.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "CaseBase");
    }

    void publishTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "publishTopic");
        topicAdapter.getTopic().setType(TopicType.PUBLISHED);
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    void createTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "createTopic");
        topicAdapter.getTopic().setType(TopicType.UNPUBLISHED);
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    void copyTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "copyTopic");
        topicAdapter.getTopic().setType(TopicType.PICK_UP);
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    void closeTopic(IReturnBool iReturnBool) {
        topicAdapter.getTopic().setFinished(true);
        topicViewModel.closeTheTopic(iReturnBool);
    }

    private void saveOrUpdate(final Mic mic, final TopicViewModel topicViewModel, final IReturnBool iReturnBool) {
        if(mic.getTopic().getSetting() != null) {
            File file = new File(mic.getTopic().getSetting().getUrl());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    mic.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    if(TextUtils.isEmpty(mic.getId())) {
                        topicViewModel.createTheTopic(iReturnBool);
                    } else {
                        topicViewModel.updateTheTopic(iReturnBool);
                    }
                }
            });
        } else {
            if(TextUtils.isEmpty(mic.getId())) {
                topicViewModel.createTheTopic(iReturnBool);
            } else {
                topicViewModel.updateTheTopic(iReturnBool);
            }
        }
    }

    protected void addLine(Line line) {
        line.setWho(user);
        topicAdapter.addLine(line);
        recyclerView.smoothScrollToPosition(topicAdapter.getItemCount()-1);
    }

    protected void addLines2Adapter(List<Line> lines) {
        topicAdapter.setItems(lines);
    }

}
