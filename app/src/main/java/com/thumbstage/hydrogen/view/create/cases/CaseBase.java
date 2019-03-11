package com.thumbstage.hydrogen.view.create.cases;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;

import java.util.List;

public abstract class CaseBase implements ITopicFragmentFunction {

    final String TAG = "CaseBase";
    Topic topic = new Topic();
    Mic mic = new Mic(null);
    TopicAdapter topicAdapter;
    LinearLayoutManager layoutManager;
    ImageView backgroundView;

    public CaseBase setMic(Mic mic) {
        this.mic = mic;
        return this;
    }

    public CaseBase setTopic(@NonNull Topic topic) {
        if( topic != null ) {
            this.topic = topic;
            setSetting(topic.getSetting());
            addLines2Adapter(topic.getDialogue());
        } else {
            addLines2Adapter(this.topic.getDialogue());
        }
        return this;
    }

    protected void setSetting(Setting setting) {
        if(setting != null) {
            Glide.with(backgroundView.getContext()).load(setting.getUrl()).into(backgroundView);
        }
    }
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

    protected void addLine(Line line) {
        addLine2Pipe(line);
        addLine2Adapter(line);
    }

    protected void addLine2Pipe(Line line) {
        LCRepository.sendLine(mic, line, new LCRepository.IReturnBool() {
            @Override
            public void callback(Boolean isOK) {
                if(isOK)  {
                    Log.i(TAG, "addLine2Pipe ok");
                } else {
                    // TODO: 2/28/2019 diagnose a fault
                }
            }
        });
    }

    protected void addLine2Adapter(Line line) {
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
