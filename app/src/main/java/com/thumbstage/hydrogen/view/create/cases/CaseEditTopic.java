package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanCloseTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPlayTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;

public class CaseEditTopic extends CaseBase implements ICanPopupMenu, ICanPlayTopic,
        ICanSetSetting, ICanPublishTopic, ICanCloseTopic {

    /*
    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_edit, menu);
    }
    */

    @Override
    public void closeTopic(IReturnBool iReturnBool) {
        topicViewModel.closeTheTopic(iReturnBool);
    }

    @Override
    public void playTopic() {

    }

    @Override
    public void publishTopic(IReturnBool iReturnBool) {

    }

    @Override
    public void setSetting(Fragment fragment) {

    }

    @Override
    public void popupMenu(@NonNull Context context, @NonNull View anchor) {

    }
}
