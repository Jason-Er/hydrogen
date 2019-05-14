package com.thumbstage.hydrogen.view.create.cases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.HyFile;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.create.assist.AssistDialogFragment;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanOpenTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaseBase implements ITopicFragmentFunction,
        ICanOpenTopic, ICanCloseTopic, ICanSetSetting, ICanAddMember, ICanPublishTopic, ICanPopupMenu {

    final String TAG = "CaseBase";

    TopicAdapter topicAdapter;
    LinearLayoutManager layoutManager;
    ImageView backgroundView;
    TopicViewModel topicViewModel;
    RecyclerView recyclerView;
    User user;
    PopupWindowAdapter popupWindowAdapter;
    ProgressBar spinner;

    HyMenuItem settingItem = new HyMenuItem(R.drawable.ic_menu_setting_g, CanOnTopic.SETUPINFO);
    HyMenuItem membersItem = new HyMenuItem(R.drawable.ic_menu_account_plus, CanOnTopic.PARTICIPANT);
    HyMenuItem openItem = new HyMenuItem(R.drawable.ic_menu_start_g, CanOnTopic.OPEN);
    HyMenuItem publishItem = new HyMenuItem(R.drawable.ic_menu_publish_g, CanOnTopic.PUBLISH);
    HyMenuItem closeItem = new HyMenuItem(R.drawable.ic_menu_transcribe_close_g, CanOnTopic.CLOSE);
    HyMenuItem updateItem = new HyMenuItem(R.drawable.ic_menu_update_g, CanOnTopic.UPDATE);


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

    public CaseBase setSpinner(ProgressBar spinner) {
        this.spinner = spinner;
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

    @Override
    public void setSetting(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.INFO.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "CaseBase");
    }

    @Override
    public void addMember(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(RequestResultCode.BottomSheetTab.class.getName(), RequestResultCode.BottomSheetTab.MEMBER.name());
        AssistDialogFragment dialog = new AssistDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(fragment.getChildFragmentManager(), "CaseBase");
    }

    @Override
    public void publishTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "publishTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.LITERAL);}});
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    @Override
    public void openTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "openTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.SEMINAR);}});
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    void copyTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "copyTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.SEMINAR);add(TopicTag.FOLLOW);}});
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    @Override
    public void closeTopic(IReturnBool iReturnBool) {
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
        // speak one line
        topicViewModel.speakLine(line, new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {

            }
        });
        topicAdapter.addLine(line);
        recyclerView.smoothScrollToPosition(topicAdapter.getItemCount()-1);
    }

    protected void addLines2Adapter(List<Line> lines) {
        topicAdapter.setItems(lines);
    }

    protected Set<CanOnTopic> getUserCan() {
        Set<CanOnTopic> userCan = null;
        if(topicAdapter.getTopic().getUserCan()!= null) {
            if(topicAdapter.getTopic().getUserCan().containsKey(user.getId())) {
                userCan = topicAdapter.getTopic().getUserCan().get(user.getId());
            }
        }
        return userCan;
    }

    @Override
    public void setUpPopupMenu() {
        List<HyMenuItem> itemList = new ArrayList<>();
        Set<CanOnTopic> userCan = getUserCan();
        if(userCan != null) {
            for(CanOnTopic can: userCan) {
                switch (can) {
                    case PUBLISH:
                        itemList.add(publishItem);
                        break;
                    case OPEN:
                        itemList.add(openItem);
                        break;
                    case CLOSE:
                        itemList.add(closeItem);
                        break;
                    case DELETE:
                        break;
                    case UPDATE:
                        itemList.add(updateItem);
                        break;
                    case PARTICIPANT:
                        itemList.add(membersItem);
                        break;
                    case SETUPINFO:
                        itemList.add(settingItem);
                        break;
                }
            }
        }
        popupWindowAdapter.setItemList(itemList);
    }

}
