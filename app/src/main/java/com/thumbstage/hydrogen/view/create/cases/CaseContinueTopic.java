package com.thumbstage.hydrogen.view.create.cases;

import android.util.Log;

import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;

public class CaseContinueTopic extends CaseBase {

    @Override
    protected void addLine(Line line) {
        super.addLine(line);
        topicViewModel.flushMicBuf(new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {
                // TODO: 4/3/2019 change line state from sending to already sent
                if(isOK) {
                    Log.i("CaseContinueTopic","addLine ia already sent ok");
                }
            }
        });
    }

}
