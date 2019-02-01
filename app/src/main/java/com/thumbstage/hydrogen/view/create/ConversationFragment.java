package com.thumbstage.hydrogen.view.create;

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

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.utils.LogUtils;
import com.thumbstage.hydrogen.utils.NotificationUtils;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationFragment extends Fragment {

    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_conversation_pullrefresh)
    SwipeRefreshLayout refreshLayout;

    AVIMConversation imConversation;
    LinearLayoutManager layoutManager;
    ConversationRecyclerAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setEnabled(false);
        itemAdapter = new ConversationRecyclerAdapter();
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
    public void onResponseMessageEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                sendText((String) event.getData());
                break;
            case "voice":
                break;
        }
    }

    public void setConversation(final AVIMConversation conversation) {
        imConversation = conversation;
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

    protected void sendText(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        sendMessage(message);
    }

    public void sendMessage(AVIMMessage message) {
        sendMessage(message, true);
    }

    public void sendMessage(AVIMMessage message, boolean addToList) {
        if (addToList) {
            itemAdapter.addMessage(message);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();

        AVIMMessageOption option = new AVIMMessageOption();
        option.setReceipt(true);
        imConversation.sendMessage(message, option, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                itemAdapter.notifyDataSetChanged();
                if (null != e) {
                    LogUtils.logException(e);
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
}
