package com.thumbstage.hydrogen.view.create.fragment;

import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class TopicAdapter extends ListDelegationAdapter {

    Mic mic;

    enum view_type {
        LINE_TEXT_CENTER, LINE_TEXT_LEFT, LINE_TEXT_RIGHT,
        LINE_AUDIO_CENTER, LINE_AUDIO_LEFT, LINE_AUDIO_RIGHT
    }

    public TopicAdapter() {
        delegatesManager.addDelegate(new LineTextLeftDelegate(view_type.LINE_TEXT_LEFT.ordinal()));
        delegatesManager.addDelegate(new LineTextRightDelegate(view_type.LINE_TEXT_RIGHT.ordinal()));
        delegatesManager.addDelegate(new LineTextCenterDelegate(view_type.LINE_TEXT_CENTER.ordinal()));
    }

    public void setMic(Mic mic) {
        this.mic = mic;
        setItems(mic.getTopic().getDialogue());
    }

    public Mic getMic() {
        return mic;
    }

    public Topic getTopic() {
        return mic.getTopic();
    }
}
