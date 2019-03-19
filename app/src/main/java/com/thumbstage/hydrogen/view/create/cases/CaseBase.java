package com.thumbstage.hydrogen.view.create.cases;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import java.util.List;

public abstract class CaseBase implements ITopicFragmentFunction {

    final String TAG = "CaseBase";

    TopicAdapter topicAdapter;
    LinearLayoutManager layoutManager;
    ImageView backgroundView;
    TopicViewModel viewModel;

    public CaseBase setTopicAdapter(TopicAdapter topicAdapter) {
        this.topicAdapter = topicAdapter;
        return this;
    }

    public CaseBase setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public CaseBase setBackgroundView(ImageView backgroundView) {
        this.backgroundView = backgroundView;
        return this;
    }

    public CaseBase setViewModel(TopicViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }


    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    protected void setSetting(Setting setting) {
        if(setting != null) {
            Glide.with(backgroundView.getContext()).load(setting.getUrl()).into(backgroundView);
        }
    }

    protected void addLine(Line line) {
        viewModel.addLine(line);
        topicAdapter.addItems(line);
        scrollToBottom();
    }

    protected void addLines2Adapter(List<Line> lines) {
        topicAdapter.setItems(lines);
    }

    protected void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(topicAdapter.getItemCount() - 1, 0);
    }

}
