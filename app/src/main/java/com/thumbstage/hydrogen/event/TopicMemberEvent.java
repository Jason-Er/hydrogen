package com.thumbstage.hydrogen.event;

import android.view.View;

import com.thumbstage.hydrogen.model.User;

public class TopicMemberEvent extends BaseEvent {

    View view;
    User user;

    public TopicMemberEvent(String message) {
        super(message);
    }

    public TopicMemberEvent(String message, View view, User user) {
        super(message);
        this.view = view;
        this.user = user;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
