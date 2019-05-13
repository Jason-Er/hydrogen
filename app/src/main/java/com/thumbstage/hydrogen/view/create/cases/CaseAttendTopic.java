package com.thumbstage.hydrogen.view.create.cases;

import android.text.TextUtils;
import android.view.View;

import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.Line;

public class CaseAttendTopic extends CaseBase {

    @Override
    protected void addLine(final Line line) {
        if(TextUtils.isEmpty(topicAdapter.getMic().getId())) {
            spinner.setVisibility(View.VISIBLE);
            copyTopic(new IReturnBool() {
                @Override
                public void callback(Boolean isOK) {
                    if(isOK) {
                        line.setWho(user);
                        topicViewModel.speakLine(line, new IReturnBool() {
                            @Override
                            public void callback(Boolean isOK) {

                            }
                        });
                        topicAdapter.addLine(line);
                        recyclerView.smoothScrollToPosition(topicAdapter.getItemCount() - 1);
                    }
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }
}
