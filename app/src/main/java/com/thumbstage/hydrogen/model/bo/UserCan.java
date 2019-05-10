package com.thumbstage.hydrogen.model.bo;

import java.util.List;

// user can do on one topic
public class UserCan {
    String userId;
    List<CanOnTopic> userCan;

    public UserCan(String userId, List<CanOnTopic> userCan) {
        this.userId = userId;
        this.userCan = userCan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CanOnTopic> getUserCan() {
        return userCan;
    }

    public void setUserCan(List<CanOnTopic> userCan) {
        this.userCan = userCan;
    }
}
