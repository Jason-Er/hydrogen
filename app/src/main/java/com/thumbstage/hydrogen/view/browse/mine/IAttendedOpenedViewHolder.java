package com.thumbstage.hydrogen.view.browse.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.BrowseItemEvent;
import com.thumbstage.hydrogen.model.bo.Mic;
import com.thumbstage.hydrogen.model.bo.Topic;
import com.thumbstage.hydrogen.model.bo.User;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IAttendedOpenedViewHolder extends RecyclerView.ViewHolder {

    Mic mic;

    @BindView(R.id.item_browse_topic_setting)
    ImageView setting;
    @BindView(R.id.item_browse_topic_name)
    TextView name;
    @BindView(R.id.item_browse_topic_brief)
    TextView brief;
    @BindView(R.id.item_browse_topic_avatar)
    ImageView avatar;

    public IAttendedOpenedViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BrowseItemEvent(mic, IAttendedOpenedViewHolder.class.getSimpleName()));
            }
        });
    }

    public void setMic(Mic mic) {
        this.mic = mic;
        Topic topic = mic.getTopic();
        name.setText(topic.getName());
        brief.setText(topic.getBrief());
        if(topic.getSetting() != null) {
            Glide.with(setting.getContext()).load(topic.getSetting().getUrl()).into(setting);
        }
        User user = topic.getStarted_by();
        if(user != null) {
            Glide.with(avatar).load(user.getAvatar()).into(avatar);
        }
    }

}
