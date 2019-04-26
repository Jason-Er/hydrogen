package com.thumbstage.hydrogen.view.create.assist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicMemberEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicMemberPlusViewHolder extends RecyclerView.ViewHolder {

    public TopicMemberPlusViewHolder(@NonNull final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new TopicMemberEvent("Plus"));
            }
        });
    }

    @OnClick(R.id.item_member_plus)
    public void plusClick(View view) {
        EventBus.getDefault().post(new TopicMemberEvent("Plus"));
    }

}
