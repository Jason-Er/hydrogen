package com.thumbstage.hydrogen.view.create.cases;

import android.text.TextUtils;

import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.Line;

public class CaseAttendTopic extends CaseBase {

    @Override
    protected void addLine(final Line line) {
        if(TextUtils.isEmpty(topicAdapter.getMic().getId())) {
            copyTopic(new IReturnBool() {
                @Override
                public void callback(Boolean isOK) {
                    if(isOK) {
                        CaseAttendTopic.super.addLine(line);
                    }
                }
            });
        } else {
            super.addLine(line);
        }
    }
}
