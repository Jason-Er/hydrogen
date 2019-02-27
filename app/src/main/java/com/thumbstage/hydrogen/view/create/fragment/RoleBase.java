package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.LogUtils;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class RoleBase implements ITopicFragmentFunction{

    final String TAG = "RoleBase";
    Topic topic = new Topic();
    Pipe pipe = new Pipe(null);
    TopicRecyclerAdapter itemAdapter;
    LinearLayoutManager layoutManager;

    public RoleBase setPipe(Pipe pipe) {
        this.pipe = pipe;
        return this;
    }

    public RoleBase setTopic(Topic topic) {
        this.topic = topic;
        return this;
    }

    public RoleBase setItemAdapter(TopicRecyclerAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
        return this;
    }

    public RoleBase setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    protected void sendLine(Line line) {

    }

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
        itemAdapter.addMessage(message);
    }

    protected void sendMessage(final AVIMMessage message, boolean addToList) {
        if (addToList) {
            itemAdapter.addMessage(message);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
        if( pipe != null) {
            AVIMMessageOption option = new AVIMMessageOption();
            option.setReceipt(true);
            final AVIMConversation imConversation = UserGlobal.getInstance().getConversation(pipe.getId());
            imConversation.sendMessage(message, option, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    itemAdapter.notifyDataSetChanged();
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

    protected void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }

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
}
