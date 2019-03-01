package com.thumbstage.hydrogen.view.create.cases;

import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;

public class CaseContinueTopic extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
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
    public void sign() {

    }

    @Override
    public void settings() {

    }

    @Override
    public void startTopic() {

    }

    @Override
    public void publishTopic() {

    }
    //endregion
}
