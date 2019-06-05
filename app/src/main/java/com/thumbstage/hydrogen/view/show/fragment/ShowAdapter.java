package com.thumbstage.hydrogen.view.show.fragment;

import android.support.annotation.NonNull;

import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Topic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.view.common.ListDelegationAdapter;

import java.util.List;

public class ShowAdapter extends ListDelegationAdapter {

    Mic mic;

    enum view_type {
        PARTICIPANT
    }

    public ShowAdapter() {
        delegatesManager.addDelegate(new ParticipantDelegate(view_type.PARTICIPANT.ordinal()));
    }

    public void setMic(@NonNull Mic mic) {
        this.mic = mic;
        if(mic.getTopic() == null
                || mic.getTopic().getMembers() == null
                || mic.getTopic().getMembers().size() == 0) {
            throw new IllegalStateException("no user available");
        }
        setMembers(mic.getTopic().getMembers());
    }

    private void setMembers(List<User> members) {
        setItems(members);
    }

    public Mic getMic() {
        return mic;
    }

    public Topic getTopic() {
        return mic.getTopic();
    }

}
