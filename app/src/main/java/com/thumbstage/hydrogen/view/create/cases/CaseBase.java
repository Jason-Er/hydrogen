package com.thumbstage.hydrogen.view.create.cases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.create.assist.AssistDialogFragment;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanOpenTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.view.create.feature.ICanUpdateTopic;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaseBase implements ITopicFragmentFunction,
        ICanOpenTopic, ICanCloseTopic, ICanUpdateTopic, ICanSetSetting, ICanAddMember, ICanPublishTopic, ICanPopupMenu {

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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.i(TAG, "handleBottomBarEvent time:"+sdf.format(((Line)event.getData()).getWhen()));
                addLine((Line) event.getData());
                break;
            case "audio":
                addLine((Line) event.getData());
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
    public void publishTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "publishTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.LITERAL);}});
        spinner.setVisibility(View.VISIBLE);
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {
                if(isOK) {
                    popupWindowAdapter.removeItem(publishItem);
                    popupWindowAdapter.removeItem(openItem);
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
                spinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void openTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "openTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.SEMINAR);}});
        spinner.setVisibility(View.VISIBLE);
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {
                if(isOK) {
                    popupWindowAdapter.removeItem(publishItem);
                    popupWindowAdapter.removeItem(openItem);
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
                spinner.setVisibility(View.GONE);
            }
        });
    }

    void copyTopic(IReturnBool iReturnBool) {
        Log.i(TAG, "copyTopic");
        topicAdapter.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.SEMINAR);add(TopicTag.FOLLOW);}});
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    @Override
    public void closeTopic(final IReturnBool iReturnBool) {
        topicAdapter.getTopic().setFinished(true);
        spinner.setVisibility(View.VISIBLE);
        topicViewModel.closeTheTopic(new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {
                if(isOK) {
                    popupWindowAdapter.removeItem(closeItem);
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
                spinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void updateTopic(IReturnBool iReturnBool) {
        saveOrUpdate(topicAdapter.getMic(), topicViewModel, iReturnBool);
    }

    private void saveOrUpdate(final Mic mic, final TopicViewModel topicViewModel, final IReturnBool iReturnBool) {
        topicAdapter.getMic().setUpdateAt(new Date());
        topicAdapter.getMic().getTopic().setUpdateAt(new Date());
        if(TextUtils.isEmpty(mic.getId())) {
            topicViewModel.createTheTopic(iReturnBool);
        } else {
            topicViewModel.updateTheTopic(iReturnBool);
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
                        if(!topicAdapter.getTopic().getTags().contains(TopicTag.LITERAL)) {
                            itemList.add(publishItem);
                        }
                        break;
                    case OPEN:
                        if(!topicAdapter.getTopic().getTags().contains(TopicTag.SEMINAR)) {
                            itemList.add(openItem);
                        }
                        break;
                    case CLOSE:
                        if(!topicAdapter.getTopic().isFinished()) {
                            itemList.add(closeItem);
                        }
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
