package com.thumbstage.hydrogen.view.create.cases;

import android.util.Log;

import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;

public class CaseContinueTopic extends CaseBase implements ICanPopupMenu,
        ICanCloseTopic {

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
    public void closeTopic(IReturnBool iReturnBool) {
        super.closeTopic(iReturnBool);
    }

    @Override
    public void setUpPopupMenu() {
        /*
        List<HyMenuItem> itemList = new ArrayList<>();
        for(CanOnMic canOnMic: canOnMics) {
            switch (canOnMic) {
                case OPEN:
                    break;
                case CLOSE:
                    itemList.add(closeItem);
                    break;
            }
        }
        popupWindowAdapter.setItemList(itemList);
        */
    }

}
