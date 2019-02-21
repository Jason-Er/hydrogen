package com.thumbstage.hydrogen.view.browse.published;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.create.CreateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublishedOpenedViewHolder extends RecyclerView.ViewHolder {

    Topic topic;

    @BindView(R.id.item_browse_topic_setting)
    ImageView setting;
    @BindView(R.id.item_browse_topic_name)
    TextView name;
    @BindView(R.id.item_browse_topic_brief)
    TextView brief;

    public PublishedOpenedViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateActivity.class);
                intent.putExtra("Topic", topic);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
        name.setText(topic.getName());
        brief.setText(topic.getBrief());
        Glide.with(setting.getContext()).load(topic.getSetting().getUrl()).into(setting);
    }

}
