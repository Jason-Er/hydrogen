package com.thumbstage.hydrogen.view.create.cases;

import android.util.Log;

import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;

public class CaseContinueTopic extends CaseBase implements ICanPopupMenu {

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

    @Override
    public void setUpPopupMenu(PopupWindowAdapter adapter) {

    }
}
