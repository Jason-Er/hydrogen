package com.thumbstage.hydrogen.view.create.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.HyMenuItemEvent;
import com.thumbstage.hydrogen.event.IMMessageEvent;
import com.thumbstage.hydrogen.event.PopupMenuEvent;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.event.TopicItemEvent;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
import com.thumbstage.hydrogen.model.dto.MicTopic;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.view.create.cases.CaseAttendTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseBase;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanOpenTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.view.create.feature.ICanUpdateTopic;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TopicFragment extends Fragment {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_topic_bk)
    ImageView background;
    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_topic_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;
    UserViewModel userViewModel;
    ListPopupWindow popupWindow;
    PopupWindowAdapter popupWindowAdapter;

    Map<TopicHandleType, CaseBase> roleMap = new HashMap<TopicHandleType, CaseBase>(){
        {
            put(TopicHandleType.CREATE, new CaseBase());
            put(TopicHandleType.ATTEND, new CaseAttendTopic());
            put(TopicHandleType.CONTINUE, new CaseBase());
        }
    };

    CaseBase currentRole = null;
    LinearLayoutManager layoutManager;
    TopicAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

        refreshLayout.setEnabled(false);
        topicAdapter = new TopicAdapter();
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(topicAdapter);

        popupWindow = new ListPopupWindow(getContext());
        popupWindowAdapter = new PopupWindowAdapter();
        popupWindow.setAdapter(popupWindowAdapter);
        popupWindow.setWidth(DensityUtil.dp2px(getContext(),200));
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setModal(true);

        spinner.setVisibility(View.VISIBLE);

        for(CaseBase caseBase: roleMap.values()) {
            caseBase.setLayoutManager(layoutManager)
                    .setTopicAdapter(topicAdapter)
                    .setBackgroundView(background)
                    .setRecyclerView(recyclerView)
                    .setSpinner(spinner)
                    .setPopupWindowAdapter(popupWindowAdapter);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureDagger();
        configureViewModel();
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        MicTopic micTopic = getActivity().getIntent().getParcelableExtra(MicTopic.class.getSimpleName());
        String handleType = getActivity().getIntent().getStringExtra(TopicHandleType.class.getSimpleName());
        if(TextUtils.isEmpty(handleType)) {
            throw new IllegalArgumentException("No TopicHandleType found!");
        }
        topicViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(TopicViewModel.class);
        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(UserViewModel.class);

        topicAdapter.setUser(userViewModel.getCurrentUser());
        for(CaseBase caseBase: roleMap.values()) {
            caseBase.setUser(userViewModel.getCurrentUser());
        }

        for(CaseBase caseBase: roleMap.values()) {
            caseBase.setTopicViewModel(topicViewModel);
        }

        switch (TopicHandleType.valueOf(handleType)) {
            case CREATE:
                currentRole = roleMap.get(TopicHandleType.CREATE);
                topicViewModel.createTopic().observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        topicAdapter.setMic(mic);
                        spinner.setVisibility(View.GONE);
                        topicAdapter.getTopic().setUserCan(setupDefaultCanOnTopic(userViewModel.getCurrentUser().getPrivileges()));
                        getActivity().invalidateOptionsMenu();
                    }
                });
                break;
            case ATTEND:
                currentRole = roleMap.get(TopicHandleType.ATTEND);
                topicViewModel.attendTopic(micTopic.getMicId()).observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        if(mic != null) {
                            topicAdapter.setMic(mic);
                            if (mic.getTopic().getSetting() != null) {
                                Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                            }
                            spinner.setVisibility(View.GONE);
                            getActivity().invalidateOptionsMenu();
                        }
                    }
                });
                break;
            case CONTINUE:
                currentRole = roleMap.get(TopicHandleType.CONTINUE);
                topicViewModel.pickUpTopic(micTopic).observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        Log.i(TAG, "CONTINUE onChanged");
                        if(mic != null) {
                            topicAdapter.setMic(mic);
                            smoothToBottom();
                            if (mic.getTopic().getSetting() != null) {
                                Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                            }
                            topicViewModel.micHasNew(new MicHasNew(mic.getId(), false));
                            spinner.setVisibility(View.GONE);
                            getActivity().invalidateOptionsMenu();
                        }
                    }
                });
                break;
        }
    }

    private Map<String, Set<CanOnTopic>> setupDefaultCanOnTopic(Set<Privilege> userPrivileges) {
        Map<String, Set<CanOnTopic>> setMap = new HashMap<>();
        Set<CanOnTopic> onTopics = new LinkedHashSet<>();
        for(Privilege privilege: userPrivileges) {
            switch (privilege) {
                case CREATE_SEMINAR:
                    onTopics.add(CanOnTopic.PARTICIPANT);
                    onTopics.add(CanOnTopic.SETUPINFO);
                    onTopics.add(CanOnTopic.OPEN);
                    break;
                case CREATE_TOPIC:
                    onTopics.add(CanOnTopic.PARTICIPANT);
                    onTopics.add(CanOnTopic.SETUPINFO);
                    onTopics.add(CanOnTopic.PUBLISH);
                    break;
            }
        }
        setMap.put(userViewModel.getCurrentUser().getId(), onTopics);
        return setMap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final PopupMenuEvent event) {
        switch (event.getMessage()) {
            case "addUser":
                User user = (User) event.getData();
                userViewModel.addContact(user);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final IMMessageEvent event) {
        if(event.getMessage().equals("onMessage")) {
            IMMessage imMessage = (IMMessage) event.getData();
            if( !imMessage.getMicTopic().getMicId().equals(topicAdapter.getMic().getId()) ) {
                EventBus.getDefault().post(new NoSubscriberEvent(EventBus.getDefault(), event));
            } else {
                topicAdapter.showIMMessage(imMessage);
                smoothToBottom();
            }
        }
    }

    private void smoothToBottom() {
        if( topicAdapter.getItemCount() > 0 ) {
            recyclerView.smoothScrollToPosition(topicAdapter.getItemCount() - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final TopicItemEvent event) {
        switch (event.getMessage()) {
            case "LineAudioFinish":
                int position = ((RecyclerView.ViewHolder) event.getData()).getAdapterPosition();
                Log.i(TAG, "LineAudioFinish position: "+position);
                if(position < recyclerView.getAdapter().getItemCount()) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position + 1);
                    if(viewHolder instanceof IExecuteSequentially) {
                        ((IExecuteSequentially) viewHolder).execute();
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final TopicBottomBarEvent event) {
        currentRole.handleBottomBarEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final HyMenuItemEvent event) {
        switch ((CanOnTopic) event.getData()) {
            case SETUPINFO:
                if( currentRole instanceof ICanSetSetting) {
                    ((ICanSetSetting) currentRole).setSetting(this);
                }
                popupWindow.dismiss();
                break;
            case OPEN:
                if( currentRole instanceof ICanOpenTopic) {
                    ((ICanOpenTopic) currentRole).openTopic(new IReturnBool() {
                        @Override
                        public void callback(Boolean status) {

                        }
                    });
                }
                popupWindow.dismiss();
                break;
            case PUBLISH:
                if( currentRole instanceof ICanPublishTopic) {
                    ((ICanPublishTopic) currentRole).publishTopic(new IReturnBool() {
                        @Override
                        public void callback(Boolean status) {

                        }
                    });
                }
                popupWindow.dismiss();
                break;
            case CLOSE:
                if(currentRole instanceof ICanCloseTopic) {
                    ((ICanCloseTopic) currentRole).closeTopic(new IReturnBool() {
                        @Override
                        public void callback(Boolean isOK) {

                        }
                    });
                }
                popupWindow.dismiss();
                break;
            case PARTICIPANT:
                if(currentRole instanceof ICanAddMember) {
                    ((ICanAddMember) currentRole).addMember(this);
                }
                popupWindow.dismiss();
                break;
            case UPDATE:
                if(currentRole instanceof ICanUpdateTopic) {
                    ((ICanUpdateTopic) currentRole).updateTopic(new IReturnBool() {
                        @Override
                        public void callback(Boolean isOK) {

                        }
                    });
                }
                popupWindow.dismiss();
                break;
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItemSetup = menu.findItem(R.id.menu_item_setup);
        menuItemSetup.setVisible(false);
        if(topicAdapter.getTopic()!=null) {
            Map<String, Set<CanOnTopic>> userCan = topicAdapter.getTopic().getUserCan();
            if(userCan != null && userCan.containsKey(userViewModel.getCurrentUser().getId())) {
                if(userCan.get(userViewModel.getCurrentUser().getId()).size() > 0) {
                    menuItemSetup.setVisible(true);
                    if( currentRole instanceof ICanPopupMenu ) {
                        ((ICanPopupMenu) currentRole).setUpPopupMenu();
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_default, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_setup:
                Log.i(TAG, "menu_item_setup");
                View anchor = getActivity().findViewById(R.id.menu_item_setup);
                popupWindow.setAnchorView(anchor);
                popupWindow.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
