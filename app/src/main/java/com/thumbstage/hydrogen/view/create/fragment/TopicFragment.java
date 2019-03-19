package com.thumbstage.hydrogen.view.create.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;
import com.thumbstage.hydrogen.view.create.cases.CaseAttendTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseBase;
import com.thumbstage.hydrogen.view.create.cases.CaseContinueTopic;
import com.thumbstage.hydrogen.view.create.cases.CaseCreateTopicItem;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

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

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel viewModel;

    Map<TopicHandleType, CaseBase> roleMap = new HashMap<TopicHandleType, CaseBase>(){
        {
            put(TopicHandleType.CREATE, new CaseCreateTopicItem());
            put(TopicHandleType.ATTEND, new CaseAttendTopic());
            put(TopicHandleType.CONTINUE, new CaseContinueTopic());
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

        refreshLayout.setEnabled(false);
        topicAdapter = new TopicAdapter();
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(topicAdapter);

        for(CaseBase caseBase: roleMap.values()) {
            caseBase.setLayoutManager(layoutManager);
            caseBase.setTopicAdapter(topicAdapter);
            caseBase.setBackgroundView(background);
        }

        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
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
        TopicEx topicEx = getActivity().getIntent().getParcelableExtra(TopicEx.class.getSimpleName());
        String handleType = getActivity().getIntent().getStringExtra(TopicHandleType.class.getSimpleName());
        if( handleType == null) {
            throw new IllegalArgumentException("No TopicHandleType found!");
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TopicViewModel.class);
        viewModel.getTopic().observe(this, new Observer<Topic>() {
            @Override
            public void onChanged(@Nullable Topic topic) {
                topicAdapter.setTopic(topic);
            }
        });

        for(CaseBase caseBase: roleMap.values()) {
            caseBase.setViewModel(viewModel);
        }

        switch (TopicHandleType.valueOf(handleType)) {
            case CREATE:
                currentRole = roleMap.get(TopicHandleType.CREATE);
                viewModel.createTopic();
                break;
            case ATTEND:
                currentRole = roleMap.get(TopicHandleType.ATTEND);
                viewModel.attendTopic(topicEx.getTopic());
                break;
            case CONTINUE:
                currentRole = roleMap.get(TopicHandleType.CONTINUE);
                viewModel.continueTopic(topicEx.getTopic(), topicEx.getMic());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final ConversationBottomBarEvent event) {
        currentRole.handleBottomBarEvent(event);
    }

    /*
    public void attendTopic(Topic topic) {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.ATTEND);
        currentRole.setBackgroundView(background)
                .setTopicAdapter(topicAdapter)
                .setLayoutManager(layoutManager)
                .setTopic(topic);
    }

    public void createTopic() {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.CREATE);
        currentRole.setBackgroundView(background)
                .setTopicAdapter(topicAdapter)
                .setLayoutManager(layoutManager)
                .setTopic(null);
    }

    public void continueTopic(Topic topic, Mic mic) {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.CONTINUE);
        currentRole.setBackgroundView(background)
                .setTopicAdapter(topicAdapter)
                .setLayoutManager(layoutManager)
                .setMic(mic)
                .setTopic(topic);
    }
    */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if( currentRole instanceof ICreateCustomize) {
            ((ICreateCustomize) currentRole).createOptionsMenu(menu, inflater);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_sign:
                Log.i(TAG, "menu_create_sign");
                if( currentRole instanceof ICreateMenuItemFunction) {
                    ((ICreateMenuItemFunction) currentRole).sign(getContext());
                }
                break;
            case R.id.menu_create_setting:
                Log.i(TAG, "menu_create_setting");
                if( currentRole instanceof ICreateMenuItemFunction) {
                    ((ICreateMenuItemFunction) currentRole).settings(this);
                }
                break;
            case R.id.menu_create_start:
                Log.i(TAG, "menu_create_start");
                if( currentRole instanceof ICreateMenuItemFunction) {
                    ((ICreateMenuItemFunction) currentRole).startTopic();
                }
                ((CreateActivity)getActivity()).navigateUp();
                break;
            case R.id.menu_create_publish:
                Log.i(TAG, "menu_create_publish");
                if( currentRole instanceof ICreateMenuItemFunction) {
                    ((ICreateMenuItemFunction) currentRole).publishTopic();
                }
                ((CreateActivity)getActivity()).navigateUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
