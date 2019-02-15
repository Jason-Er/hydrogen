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
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.LogUtils;
import com.thumbstage.hydrogen.utils.NotificationUtils;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.common.ICallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicFragment extends Fragment {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_topic_pullrefresh)
    SwipeRefreshLayout refreshLayout;

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
        if(imConversation == null) {
            createConversation(new ICallBack() {
                @Override
                public void doAfter() {
                    addTopicDialogue(imConversation.getConversationId(), topic.getDialogue());
                    handleEvent(event);
                }
            });
        } else {
            handleEvent(event);
        }
    }

    private void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                sendText((String) event.getData());
                break;
            case "voice":

                break;
        }
    }

    protected void createConversation(final ICallBack iCallBack) {
        User.getInstance().getClient().createConversation(topic.getMembers(), topic.getName(), null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVIMException e) {
                if(e == null) {
                    imConversation = conversation;
                    iCallBack.doAfter();
                }
            }
        });
    }

    protected void addTopicDialogue(String conversationId, List<Line> dialogue) {
        List<Map> data = DataConvertUtil.convert2AVObject(dialogue);
        AVObject conversation = AVObject.createWithoutData("_Conversation", conversationId);
        conversation.addAllUnique("dialogue", data);
        conversation.saveInBackground();
    }

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
        if( imConversation != null) {
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
            addOneLine(imConversation.getConversationId(), DataConvertUtil.convert2Line((AVIMTypedMessage) message));
        }
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

    /*
    public List<String> getDialogue() {
        return dialogue;
    }
    */

    public void setTopic(Topic topic) {
        this.topic = topic;
        imConversation = null;
        for(Line line : topic.getDialogue()) {
            AVIMTypedMessage m;
            if( StringUtil.isUrl(line.getWhat()) ) { // default is Audio
                AVFile file = new AVFile("music", line.getWhat(), null);
                m = new AVIMAudioMessage(file);
            } else { // is text
                m = new AVIMTextMessage();
                ((AVIMTextMessage)m).setText(line.getWhat());
            }
            m.setFrom(line.getWho());
            m.setTimestamp(line.getWhen().getTime());
            sendMessage(m);
        }
    }

    public void createTopic() {

    }
}
