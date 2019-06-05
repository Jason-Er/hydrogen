package com.thumbstage.hydrogen.view.show.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantViewHolder extends RecyclerView.ViewHolder {
    User user;

    @BindView(R.id.item_show_participant_avatar)
    ImageView avatar;

    public ParticipantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setParticipant(User user) {
        this.user = user;
        Glide.with(avatar).load(user.getAvatar()).into(avatar);
    }

    public void speakLine(Line line) {

    }
}
