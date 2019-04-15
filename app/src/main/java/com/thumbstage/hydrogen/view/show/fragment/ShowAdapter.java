package com.thumbstage.hydrogen.view.show.fragment;

import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

public class ShowAdapter extends ListDelegationAdapter {

    Mic mic;

    enum view_type {
        LINE_TEXT_CENTER, LINE_TEXT_LEFT, LINE_TEXT_RIGHT,
        LINE_AUDIO_CENTER, LINE_AUDIO_LEFT, LINE_AUDIO_RIGHT
    }

    public ShowAdapter() {

    }

    public void setMic(Mic mic) {
        this.mic = mic;
    }

    public Mic getMic() {
        return mic;
    }

    public Topic getTopic() {
        return mic.getTopic();
    }

}
