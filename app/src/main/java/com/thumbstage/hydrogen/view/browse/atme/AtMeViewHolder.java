package com.thumbstage.hydrogen.view.browse.atme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.create.CreateActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatMessageInterface;

public class AtMeViewHolder extends RecyclerView.ViewHolder {

    Pipe pipe;

    @BindView(R.id.item_pipe_atme_iv_avatar)
    ImageView avatar;
    @BindView(R.id.item_pipe_atme_tv_name)
    TextView name;
    @BindView(R.id.item_pipe_atme_tv_message)
    TextView messageView;
    @BindView(R.id.item_pipe_atme_tv_time)
    TextView timeView;

    public AtMeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(v.getContext(), CreateActivity.class);
                intent.putExtra(TopicEx.class.getSimpleName(), topicEx);
                intent.putExtra(CreateActivity.TopicHandleType.class.getSimpleName(),
                        CreateActivity.TopicHandleType.ATTEND.name());
                v.getContext().startActivity(intent);
                */
            }
        });
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(pipe.getId());
        updateLastMessage(conversation.getLastMessage());
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
