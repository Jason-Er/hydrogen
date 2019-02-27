package com.thumbstage.hydrogen.view.browse.atme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMChatRoom;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMServiceConversation;
import com.avos.avoscloud.im.v2.AVIMTemporaryConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.utils.LogUtils;
import com.thumbstage.hydrogen.utils.PipeUtils;
import com.thumbstage.hydrogen.view.create.CreateActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatMessageInterface;

public class AtMeViewHolder extends RecyclerView.ViewHolder {

    Pipe pipe;

    @BindView(R.id.item_pipe_atme_iv_avatar)
    ImageView avatarView;
    @BindView(R.id.item_pipe_atme_tv_name)
    TextView nameView;
    @BindView(R.id.item_pipe_atme_tv_message)
    TextView messageView;
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
            public void onClick(View v) {
                TopicEx topicEx = new TopicEx(null, pipe);
                Intent intent = new Intent(v.getContext(), CreateActivity.class);
                intent.putExtra(TopicEx.class.getSimpleName(), topicEx);
                intent.putExtra(CreateActivity.TopicHandleType.class.getSimpleName(),
                        CreateActivity.TopicHandleType.CONTINUE.name());
                v.getContext().startActivity(intent);

            }
        });
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(pipe.getId());

        updateName(conversation);
        updateIcon(conversation);
        updateType(conversation);
        updateUnreadCount(conversation);
        updateLastMessage(conversation.getLastMessage());
    }

    private void updateIcon(AVIMConversation conversation) {
        if (null != conversation) {
            if (conversation.isTransient() || conversation.getMembers().size() > 2) {
                avatarView.setImageResource(R.drawable.ic_item_account_multiple);
            } else {
                PipeUtils.getConversationPeerIcon(conversation, new AVCallback<String>() {
                    @Override
                    protected void internalDone0(String s, AVException e) {
                        if (null != e) {
                            LogUtils.logException(e);
                        }
                        if (!TextUtils.isEmpty(s)) {
                            Glide.with(itemView.getContext()).load(s)
                                    .placeholder(R.drawable.ic_item_account).into(avatarView);
                        } else {
                            avatarView.setImageResource(R.drawable.ic_item_account);
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
                    nameView.setText(s);
                }
            }
        });
    }

    private void updateLastMessage(AVIMMessage message) {
        if (null != message) {
            Date date = new Date(message.getTimestamp());
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            timeView.setText(format.format(date));
            messageView.setText(getMessageeShorthand(messageView.getContext(), message));
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

}
