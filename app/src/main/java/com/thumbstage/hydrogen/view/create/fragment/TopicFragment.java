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
import com.thumbstage.hydrogen.model.bo.CanOnMic;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.cases.CaseCreateTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseEditTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseCopyTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseBase;
import com.thumbstage.hydrogen.view.create.cases.CaseContinueTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanAddMember;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    String micId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;
    UserViewModel userViewModel;
    ListPopupWindow popupWindow;
    PopupWindowAdapter popupWindowAdapter;

    Map<TopicHandleType, CaseBase> roleMap = new HashMap<TopicHandleType, CaseBase>(){
        {
            put(TopicHandleType.CREATE, new CaseCreateTopic());
            put(TopicHandleType.ATTEND, new CaseCopyTopic());
            put(TopicHandleType.CONTINUE, new CaseContinueTopic());
            put(TopicHandleType.EDIT, new CaseEditTopic());
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
        micId = getActivity().getIntent().getStringExtra(Mic.class.getSimpleName());
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
                    }
                });
                break;
            case ATTEND:
                currentRole = roleMap.get(TopicHandleType.ATTEND);
                topicViewModel.attendTopic(micId).observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        topicAdapter.setMic(mic);
                        if(mic.getTopic().getSetting() != null) {
                            Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                        }
                        spinner.setVisibility(View.GONE);
                    }
                });
                break;
            case CONTINUE:
                currentRole = roleMap.get(TopicHandleType.CONTINUE);
                topicViewModel.pickUpTopic(micId).observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        topicAdapter.setMic(mic);
                        recyclerView.smoothScrollToPosition(topicAdapter.getItemCount()-1);
                        if(mic.getTopic().getSetting() != null) {
                            Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                        }
                        spinner.setVisibility(View.GONE);
                    }
                });
                break;
            case EDIT:
                currentRole = roleMap.get(TopicHandleType.EDIT);
                topicViewModel.editTopic(micId).observe(this, new Observer<Mic>() {
                    @Override
                    public void onChanged(@Nullable Mic mic) {
                        topicAdapter.setMic(mic);
                        if(mic.getTopic().getSetting() != null) {
                            Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                        }
                        spinner.setVisibility(View.GONE);
                    }
                });
                break;
        }

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
            if( !imMessage.getMicId().equals(topicAdapter.getMic().getId()) ) {
                EventBus.getDefault().post(new NoSubscriberEvent(EventBus.getDefault(), event));
            } else {
                topicViewModel.refreshTheTopic();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final TopicBottomBarEvent event) {
        currentRole.handleBottomBarEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final HyMenuItemEvent event) {
        switch ((HyMenuItem.CommandType) event.getData()) {
            case SETTING:
                if( currentRole instanceof ICanSetSetting) {
                    ((ICanSetSetting) currentRole).setSetting(this);
                }
                popupWindow.dismiss();
                break;
            case START:
                if( currentRole instanceof ICanCreateTopic) {
                    ((ICanCreateTopic) currentRole).createTopic(new IReturnBool() {
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
            case MEMBERS:
                if(currentRole instanceof ICanAddMember) {
                    ((ICanAddMember) currentRole).addMember(this);
                }
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_default, menu);
        final MenuItem menuItemSetup = menu.findItem(R.id.menu_item_setup);
        userViewModel.getCanOnMic(micId, userViewModel.getCurrentUser().getId()).observe(this, new Observer<Set<CanOnMic>>() {
            @Override
            public void onChanged(@Nullable Set<CanOnMic> canOnMics) {
                Log.i(TAG, "can");
                if (canOnMics.size() > 0) {
                    menuItemSetup.setVisible(true);
                    if( currentRole instanceof ICanPopupMenu ) {
                        ((ICanPopupMenu) currentRole).setUpPopupMenu(canOnMics);
                    }
                } else {
                    menuItemSetup.setVisible(false);
                }
            }
        });
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
