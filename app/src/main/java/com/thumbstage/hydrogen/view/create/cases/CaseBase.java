package com.thumbstage.hydrogen.view.create.cases;

import android.support.v7.widget.LinearLayoutManager;

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

    }

    protected void addLine2Adapter(Line line) {
        topicAdapter.addItems(line);
        addLine2Topic(line);
    }

    protected void addLines2Adapter(List<Line> lines) {

    }

    private void addLine2Topic(Line line) {
        topic.getDialogue().add(line);
    }

    /*
    protected void sendText(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("type", LineType.LT_DIALOGUE.name());
        message.setAttrs(attributes);
        sendMessage(message);
    }

    protected void sendMessage(AVIMMessage message) {
        sendMessage(message, true);
    }

    protected void addToList(AVIMMessage message) {
        topicAdapter.addMessage(message);
    }

    protected void sendMessage(final AVIMMessage message, boolean addToList) {
        if (addToList) {
            topicAdapter.addMessage(message);
        }
        topicAdapter.notifyDataSetChanged();
        scrollToBottom();
        if( pipe != null) {
            AVIMMessageOption option = new AVIMMessageOption();
            option.setReceipt(true);
            final AVIMConversation imConversation = UserGlobal.getInstance().getConversation(pipe.getId());
            imConversation.sendMessage(message, option, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    topicAdapter.notifyDataSetChanged();
                    if (null != e) {
                        LogUtils.logException(e);
                    } else {
                        LCRepository.addTopicOneLine(pipe,
                                DataConvertUtil.convert2Line(message), new LCRepository.ICallBack() {
                                    @Override
                                    public void callback(String objectID) {
                                        Log.i(TAG, "addTopicOneLine ok");
                                    }
                                });
                        // TODO: 2/22/2019 must to synchronous loca
                        // topic.getDialogue().add(new Line()); // local topic add line
                        // EventBus.getDefault().post(new );
                    }
                }
            });

        }
    }
    */

    protected void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(topicAdapter.getItemCount() - 1, 0);
    }

    /*
    protected void appendTopicDialogue(Topic topic) {
        for(Line line : topic.getDialogue()) {
            AVIMTypedMessage m;
            if( StringUtil.isUrl(line.getWhat()) ) { // default is Audio
                AVFile file = new AVFile("music", line.getWhat(), null);
                m = new AVIMAudioMessage(file);
            } else { // is text
                m = new AVIMTextMessage();
                ((AVIMTextMessage)m).setText(line.getWhat());
            }
            m.setFrom(line.getWho());
            m.setTimestamp(line.getWhen().getTime());
            addToList(m);
        }
    }

    public void clearTopic() {
        topic = new Topic();
    }
    */
}
