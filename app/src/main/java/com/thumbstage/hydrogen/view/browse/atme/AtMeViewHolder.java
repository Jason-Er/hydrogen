package com.thumbstage.hydrogen.view.browse.atme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.fragment.TopicHandleType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtMeViewHolder extends RecyclerView.ViewHolder {

    Mic mic;

    @BindView(R.id.item_pipe_atme_iv_avatar)
    ImageView avatar;
    @BindView(R.id.item_pipe_atme_tv_name)
    TextView name;
    @BindView(R.id.item_pipe_atme_tv_message)
    TextView message;
    @BindView(R.id.item_pipe_atme_tv_type)
    TextView typeView;
    @BindView(R.id.item_pipe_atme_tv_time)
    TextView timeView;
    @BindView(R.id.item_pipe_atme_tv_unread)
    TextView unreadView;

    public AtMeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                /*
                CloudAPI.getTopic(mic, new CloudAPI.IReturnTopic() {
                    @Override
                    public void callback(Topic topic) {
                        TopicEx topicEx = new TopicEx(topic, mic);
                        Intent intent = new Intent(v.getContext(), CreateActivity.class);
                        intent.putExtra(TopicEx.class.getSimpleName(), topicEx);
                        intent.putExtra(TopicHandleType.class.getSimpleName(),
                                TopicHandleType.CONTINUE.name());
                        v.getContext().startActivity(intent);
                    }
                });
                */
            }
        });
    }

    public void setMic(Mic mic) {
        this.mic = mic;
        /*
        CloudAPI.getLastLineUser(mic, new CloudAPI.IReturnUser() {
            @Override
            public void callback(User user) {
                if(user != null) {
                    GlideUtil.inject(itemView.getContext(), user.getAvatar(), avatar);
                    name.setText(user.getName());
                }
            }
        });
        CloudAPI.getLastLine(mic, new CloudAPI.IReturnLine() {
            @Override
            public void callback(Line line) {
                if(line != null) {
                    message.setText(line.getWhat());
                    timeView.setText(StringUtil.date2String4Show(line.getWhen()));
                }
            }
        });
        */
        /*
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(mic.getId());

        updateName(conversation);
        updateIcon(conversation);
        updateType(conversation);
        updateUnreadCount(conversation);
        updateLastMessage(conversation.getLastMessage());
        */
    }

    /*
    private void updateIcon(AVIMConversation conversation) {
        if (null != conversation) {
            if (conversation.isTransient() || conversation.getMembers().size() > 2) {
                avatar.setImageResource(R.drawable.ic_item_account_multiple);
            } else {
                PipeUtils.getConversationPeerIcon(conversation, new AVCallback<String>() {
                    @Override
                    protected void internalDone0(String s, AVException e) {
                        if (null != e) {
                            LogUtils.logException(e);
                        }
                        if (!TextUtils.isEmpty(s)) {
                            Glide.with(itemView.getContext()).load(s)
                                    .placeholder(R.drawable.ic_item_account).into(avatar);
                        } else {
                            avatar.setImageResource(R.drawable.ic_item_account);
                        }
                    }
                });
            }
        }
    }

    private void updateType(AVIMConversation conversation) {
        if (conversation instanceof AVIMServiceConversation) {
            typeView.setText("S");
        } else if (conversation instanceof AVIMTemporaryConversation) {
            typeView.setText("T");
        } else if (conversation instanceof AVIMChatRoom) {
            typeView.setText("R");
        } else {
            typeView.setText("C");
        }
    }

    private void updateUnreadCount(AVIMConversation conversation) {
        int num = conversation.getUnreadMessagesCount();
        unreadView.setText(num + "");
        unreadView.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
    }

    private void updateName(final AVIMConversation conversation) {
        PipeUtils.getConversationName(conversation, new AVCallback<String>() {
            @Override
            protected void internalDone0(String s, AVException e) {
                if (null != e) {
                    LogUtils.logException(e);
                } else {
                    name.setText(s);
                }
            }
        });
    }

    private void updateLastMessage(AVIMMessage message) {
        if (null != message) {
            Date date = new Date(message.getTimestamp());
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            timeView.setText(format.format(date));
            this.message.setText(getMessageeShorthand(this.message.getContext(), message));
        }
    }

    private static CharSequence getMessageeShorthand(Context context, AVIMMessage message) {
        if (message instanceof AVIMTypedMessage) {
            AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(
                    ((AVIMTypedMessage) message).getMessageType());
            switch (type) {
                case TextMessageType:
                    return ((AVIMTextMessage) message).getText();
                case ImageMessageType:
                    return context.getString(cn.leancloud.chatkit.R.string.lcim_message_shorthand_image);
                case LocationMessageType:
                    return context.getString(cn.leancloud.chatkit.R.string.lcim_message_shorthand_location);
                case AudioMessageType:
                    return context.getString(cn.leancloud.chatkit.R.string.lcim_message_shorthand_audio);
                default:
                    CharSequence shortHand = "";
                    if (message instanceof LCChatMessageInterface) {
                        LCChatMessageInterface messageInterface = (LCChatMessageInterface) message;
                        shortHand = messageInterface.getShorthand();
                    }
                    if (TextUtils.isEmpty(shortHand)) {
                        shortHand = context.getString(cn.leancloud.chatkit.R.string.lcim_message_shorthand_unknown);
                    }
                    return shortHand;
            }
        } else {
            return message.getContent();
        }
    }
    */
}
