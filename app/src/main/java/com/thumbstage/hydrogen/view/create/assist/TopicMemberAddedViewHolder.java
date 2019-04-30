package com.thumbstage.hydrogen.view.create.assist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicMemberEvent;
import com.thumbstage.hydrogen.model.vo.User;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicMemberAddedViewHolder extends RecyclerView.ViewHolder {

    User user;

    @BindView(R.id.item_member_added_avatar)
    ImageButton avatar;

    public TopicMemberAddedViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(new TopicMemberEvent("Added", v, user));
                return true;
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new TopicMemberEvent("Added", v, user));
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null) {
            Glide.with(avatar).load(user.getAvatar()).into(avatar);
        }
    }

}
