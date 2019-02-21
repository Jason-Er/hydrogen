package com.thumbstage.hydrogen.view.create.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.ICreateActivityFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicFragment extends Fragment implements ICreateActivityFunction {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_topic_pullrefresh)
    SwipeRefreshLayout refreshLayout;

    Map<CreateActivity.TopicHandleType, RoleBase> roleMap = new HashMap<CreateActivity.TopicHandleType, RoleBase>(){
        {
            put(CreateActivity.TopicHandleType.CREATE, new RoleCreateTopic());
            put(CreateActivity.TopicHandleType.ATTEND, new RoleAttendTopic());
            put(CreateActivity.TopicHandleType.CONTINUE, new RoleContinueTopic());
        }
    };

    RoleBase currentRole = null;
    AVIMConversation imConversation;
    LinearLayoutManager layoutManager;
    TopicRecyclerAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setEnabled(false);
        itemAdapter = new TopicRecyclerAdapter();
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( itemAdapter );

        EventBus.getDefault().register(this);
        return view;
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

    public void attendTopic(Topic topic) {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.ATTEND);
        currentRole.setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
        currentRole.setTopic(topic);
    }

    public void createTopic() {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.CREATE);
        currentRole.setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
    }

    public void continueTopic(final AVIMConversation conversation) {
        currentRole = roleMap.get(CreateActivity.TopicHandleType.CONTINUE);
        currentRole.setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
    }

    // region implements interface ICreateActivityFunction
    @Override
    public void onActionOK() {
        Log.i(TAG, "onActionOK");
        if( currentRole instanceof ICreateActivityFunction ) {
            ((ICreateActivityFunction) currentRole).onActionOK();
        }
    }

    @Override
    public void onActionPublish() {
        if( currentRole instanceof ICreateActivityFunction ) {
            ((ICreateActivityFunction) currentRole).onActionPublish();
        }
    }
    // endregion
}
