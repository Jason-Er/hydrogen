package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;

public class CaseAttendTopic extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    @Override
    public void handleBottomBarEvent(final ConversationBottomBarEvent event) {
        if(TextUtils.isEmpty(mic.getId())) {
            LCRepository.copyTopicFromPublishedOpened(topic, new LCRepository.IReturnPipe() {
                @Override
                public void callback(Mic p) {
                    mic = p;
                    handleEvent(event);
                }
            });
        } else {
            handleEvent(event);
        }
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_default, menu);
    }

    // region implements interface ICreateMenuItemFunction
    @Override
    public void sign(Context context) {
        Navigation.sign(context);
    }

    @Override
    public void settings(Fragment fragment) {

    }

    @Override
    public void startTopic() {

    }

    @Override
    public void publishTopic() {

    }
    //endregion
}
