package com.thumbstage.hydrogen.view.browse.atme;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.AtMeEvent;
import com.thumbstage.hydrogen.model.bo.AtMe;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtMeViewHolder extends RecyclerView.ViewHolder {

    AtMe atMe;

    @BindView(R.id.item_mic_atme_topic_setting)
    ImageView setting;
    @BindView(R.id.item_mic_atme_avatar)
    ImageView avatar;
    @BindView(R.id.item_mic_atme_topic)
    TextView topicName;
    @BindView(R.id.item_mic_atme_what)
    TextView message;
    @BindView(R.id.item_mic_atme_time)
    TextView timeView;
    @BindView(R.id.item_mic_atme_unread)
    TextView unreadView;

    public AtMeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EventBus.getDefault().post(new AtMeEvent(atMe, "click"));
            }
        });
    }

    public void setAtMe(AtMe atMe) {
        this.atMe = atMe;
        GlideUtil.inject(itemView.getContext(), atMe.getWho().getAvatar(), avatar);
        topicName.setText(atMe.getMic().getTopic().getName());
        if(atMe.getMic().getTopic().getSetting() != null) {
            Glide.with(setting).load(atMe.getMic().getTopic().getSetting().getUrl()).into(setting);
        }
        message.setText(atMe.getWho().getName()+":"+atMe.getWhat());
        timeView.setText(StringUtil.date2String4Show(atMe.getWhen()));
        if(atMe.isBrowsed()) {
            unreadView.setVisibility(View.GONE);
        }
    }

}
