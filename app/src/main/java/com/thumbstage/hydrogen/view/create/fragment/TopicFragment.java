package com.thumbstage.hydrogen.view.create.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.User;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.LogUtils;
import com.thumbstage.hydrogen.utils.NotificationUtils;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.common.ICallBack;
import com.thumbstage.hydrogen.view.create.ICreateActivityFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicFragment extends Fragment implements ICreateActivityFunction {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_topic_pullrefresh)
    SwipeRefreshLayout refreshLayout;

    enum roleType {
        CREATE, ATTEND, CONTINUE
    }

    Map<roleType, RoleBase> roleMap = new HashMap<roleType, RoleBase>(){
        {
            put(roleType.CREATE, new RoleCreateTopic());
            put(roleType.ATTEND, new RoleAttendTopic());
            put(roleType.CONTINUE, new RoleContinueTopic());
        }
    };

    RoleBase currentRole = null;
    AVIMConversation imConversation;
    LinearLayoutManager layoutManager;
    TopicRecyclerAdapter itemAdapter;
    Topic topic;

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
        currentRole.handleBootomBarEvent(event);
    }

    /*
    protected void addOneLine(String conversationId, Line line) {
        Map data = DataConvertUtil.convert2AVObject(line);
        AVObject conversation = AVObject.createWithoutData("_Conversation", conversationId);
        conversation.addUnique("dialogue", data);
        conversation.saveInBackground();
    }

    public void setConversation(final AVIMConversation conversation) {

            imConversation = conversation;
        if( conversation != null ) { // IM state
            refreshLayout.setEnabled(true);
            fetchMessages();
            imConversation.read();
            NotificationUtils.addTag(conversation.getConversationId());
            if (!conversation.isTransient()) {
                if (conversation.getMembers().size() == 0) {
                    conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (null != e) {
                                LogUtils.logException(e);
                                Toast.makeText(getContext(), "encounter network error, please try later.", Toast.LENGTH_SHORT);
                            }
                            itemAdapter.showUserName(conversation.getMembers().size() > 2);
                        }
                    });
                } else {
                    itemAdapter.showUserName(conversation.getMembers().size() > 2);
                }
            } else {
                itemAdapter.showUserName(true);
            }
        }
    }

    private void fetchMessages() {
        imConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messageList, AVIMException e) {
                if (filterException(e)) {
                    itemAdapter.setMessageList(messageList);
                    itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
                            imConversation.getLastReadAt());
                    itemAdapter.notifyDataSetChanged();
                    scrollToBottom();
                    clearUnreadConut();
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }


    private boolean filterException(Exception e) {
        if (null != e) {
            LogUtils.logException(e);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return (null == e);
    }

    private void clearUnreadConut() {
        if (imConversation.getUnreadMessagesCount() > 0) {
            imConversation.read();
        }
    }
    */

    public void attendTopic(Topic topic) {
        currentRole = roleMap.get(roleType.ATTEND);
        currentRole.setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
        currentRole.setTopic(topic);
    }

    public void createTopic() {
        currentRole = roleMap.get(roleType.CREATE);
        currentRole.setTopic(topic)
                .setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
    }

    public void continueTopic(final AVIMConversation conversation) {
        currentRole = roleMap.get(roleType.CONTINUE);
        currentRole.setTopic(topic)
                .setImConversation(imConversation)
                .setItemAdapter(itemAdapter)
                .setLayoutManager(layoutManager);
    }

    // region implements interface ICreateActivityFunction
    @Override
    public void onActionOK() {
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
