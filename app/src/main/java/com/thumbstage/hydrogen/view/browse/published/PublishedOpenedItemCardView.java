package com.thumbstage.hydrogen.view.browse.published;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublishedOpenedItemCardView extends CardView {
    private TopicInfo topicInfo;

    @BindView(R.id.publishedOpenedItem_setting)
    ImageView setting;
    @BindView(R.id.publishedOpenedItem_name)
    TextView name;
    @BindView(R.id.publishedOpenedItem_brief)
    TextView brief;

    public PublishedOpenedItemCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
        name.setText(topicInfo.name);
        brief.setText(topicInfo.brief);
    }
}
