package com.thumbstage.hydrogen.view.common;

import com.thumbstage.hydrogen.model.bo.CanOnTopic;

public class HyMenuItem {
    int resId;
    CanOnTopic canOnTopic;

    public HyMenuItem(int resId, CanOnTopic canOnTopic) {
        this.resId = resId;
        this.canOnTopic = canOnTopic;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public CanOnTopic getCanOnTopic() {
        return canOnTopic;
    }

    public void setCanOnTopic(CanOnTopic canOnTopic) {
        this.canOnTopic = canOnTopic;
    }
}
