package com.thumbstage.hydrogen.view.create.fragment;

import android.webkit.URLUtil;

import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;
import com.thumbstage.hydrogen.view.create.type.LineTextLeft;
import com.thumbstage.hydrogen.view.create.type.LineTextRight;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends ListDelegationAdapter {

    Mic mic;
    User user; // current user

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
        setDialogue(mic.getTopic().getDialogue());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mic getMic() {
        return mic;
    }

    public Topic getTopic() {
        return mic.getTopic();
    }

    public void addLine(Line line) {
        addItem(generateLine(line));// for show
        mic.getTopic().getDialogue().add(line); // for save
    }

    public void setDialogue(List<Line> lines) {
        List<Line> dialogue = new ArrayList<>();
        for(Line line: lines) {
            dialogue.add(generateLine(line));
        }
        setItems(dialogue);
    }

    private Line generateLine(Line line) {
        Line local = null;
        if( !URLUtil.isValidUrl(line.getWhat()) ) {
            if(!line.getWho().equals(user)) {
                local = new LineTextLeft(line);
            } else {
                local = new LineTextRight(line);
            }
        } else {

        }
        return local;
    }
}
