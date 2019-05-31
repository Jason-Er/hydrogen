package com.thumbstage.hydrogen.view.create.fragment;

import android.webkit.URLUtil;

import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Topic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;
import com.thumbstage.hydrogen.view.create.type.LineAudioDirection;
import com.thumbstage.hydrogen.view.create.type.LineAudioMine;
import com.thumbstage.hydrogen.view.create.type.LineAudioOthers;
import com.thumbstage.hydrogen.view.create.type.LineEx;
import com.thumbstage.hydrogen.view.create.type.LineTextDirection;
import com.thumbstage.hydrogen.view.create.type.LineTextMine;
import com.thumbstage.hydrogen.view.create.type.LineTextOthers;

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
        delegatesManager.addDelegate(new LineTextOthersDelegate(view_type.LINE_TEXT_LEFT.ordinal()));
        delegatesManager.addDelegate(new LineTextMineDelegate(view_type.LINE_TEXT_RIGHT.ordinal()));
        delegatesManager.addDelegate(new LineTextDirectionDelegate(view_type.LINE_TEXT_CENTER.ordinal()));
        delegatesManager.addDelegate(new LineAudioOthersDelegate(view_type.LINE_AUDIO_LEFT.ordinal()));
        delegatesManager.addDelegate(new LineAudioMineDelegate(view_type.LINE_AUDIO_RIGHT.ordinal()));
        delegatesManager.addDelegate(new LineAudioDirectionDelegate(view_type.LINE_AUDIO_CENTER.ordinal()));
    }

    public void setMic(Mic mic) {
        this.mic = mic;
        if(user == null) {
            throw new IllegalStateException("no user available");
        }
        setDialogue(mic.getTopic().getDialogue());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mic getMic() {
        return mic;
    }

    public Topic getTopic() {
        return mic==null? null: mic.getTopic();
    }

    public void addLine(Line line) {
        LineEx lineEx = generateLine(line);
        determineShowTime(lineEx, getItems());
        addItem(lineEx);// for show
        mic.getTopic().getDialogue().add(line); // for save
    }

    public void showIMMessage(IMMessage imMessage) {
        Line line = convert2Line(imMessage);
        addLine(line);
    }

    private User findUser(String userId) {
        User user = null;
        for(User u: mic.getTopic().getMembers()) {
            if(u.getId().equals(userId)) {
                user = u;
                break;
            }
        }
        return user;
    }

    private Line convert2Line(IMMessage imMessage) {
        Line line = new Line();
        line.setWho(findUser(imMessage.getWhoId()));
        line.setWhen(imMessage.getWhen());
        line.setWhat(imMessage.getWhat());
        line.setLineType(imMessage.getLineType());
        return line;
    }

    public void setDialogue(List<Line> lines) {
        List<LineEx> dialogue = new ArrayList<>();
        for(Line line: lines) {
            dialogue.add(generateLine(line));
        }
        filterShowTime(dialogue);
        setItems(dialogue);
    }

    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    long nm = 1000 * 60;

    private void determineShowTime(LineEx lineEx, List<LineEx> dialogue) {
        if(!dialogue.isEmpty()) {
            LineEx pre = dialogue.get(dialogue.size()-1);
            determineShowTime(pre, lineEx);
        }
    }

    private void filterShowTime(List<LineEx> dialogue) {
        for(int i=1;i<dialogue.size();i++) {
            LineEx next = dialogue.get(i);
            LineEx pre = dialogue.get(i-1);
            determineShowTime(pre, next);
        }
    }

    private void determineShowTime(LineEx pre, LineEx next) {
        long diff = next.getWhen().getTime() - pre.getWhen().getTime();
        if(diff >= 0) {
            if(diff / nm >= 5) {
                next.setNeedShowTime(true);
            } else {
                next.setNeedShowTime(false);
            }
        } else {
            next.setNeedShowTime(false);
        }
    }

    private LineEx generateLine(Line line) {
        LineEx local = null;
        switch (line.getLineType()) {
            case LT_DIRECTION:
                if( !URLUtil.isValidUrl(line.getWhat()) ) {
                    local = new LineTextDirection(line);
                } else {// voice then
                    local = new LineAudioDirection(line);
                }
                break;
            case LT_DIALOGUE:
                if( !URLUtil.isValidUrl(line.getWhat()) ) {
                    if(!line.getWho().equals(user)) {
                        local = new LineTextOthers(line);
                    } else {
                        local = new LineTextMine(line);
                    }
                } else {// voice then
                    if(!line.getWho().equals(user)) {
                        local = new LineAudioOthers(line);
                    } else {
                        local = new LineAudioMine(line);
                    }
                }
                break;
        }
        return local;
    }
}
