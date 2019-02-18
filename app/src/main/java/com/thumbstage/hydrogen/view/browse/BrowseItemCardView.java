package com.thumbstage.hydrogen.view.browse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowseItemCardView extends CardView {
    protected Topic topic;

    @BindView(R.id.item_browse_topic_setting)
    ImageView setting;
    @BindView(R.id.item_browse_topic_name)
    TextView name;
    @BindView(R.id.item_browse_topic_brief)
    TextView brief;

    public BrowseItemCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
        name.setText(topic.getName());
        brief.setText(topic.getBrief());
        Glide.with(getContext()).load(topic.getSetting_id()).into(setting);
    }

}
