package com.thumbstage.hydrogen.view.create.cases;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.create.fragment.ITopicFragmentFunction;
import com.thumbstage.hydrogen.view.create.fragment.TopicAdapter;

import java.util.List;

public abstract class CaseBase implements ITopicFragmentFunction {

    final String TAG = "CaseBase";
    Topic topic = new Topic();
    Pipe pipe = new Pipe(null);
    TopicAdapter topicAdapter;
    LinearLayoutManager layoutManager;

    public CaseBase setPipe(Pipe pipe) {
        this.pipe = pipe;
        return this;
    }

    public CaseBase setTopic(Topic topic) {
        this.topic = topic;
        addLines2Adapter(topic.getDialogue());
        return this;
    }

    public CaseBase setTopicAdapter(TopicAdapter topicAdapter) {
        this.topicAdapter = topicAdapter;
        return this;
    }

    public CaseBase setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    protected void addLine(Line line) {
        addLine2Pipe(line);
        addLine2Adapter(line);
    }

    protected void addLine2Pipe(Line line) {
        LCRepository.sendLine(pipe, line, new LCRepository.IReturnBool() {
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
