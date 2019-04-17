package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;

public class CaseCopyTopic extends CaseBase implements ICanCreateTopic, ICanPopupMenu {

    /*
    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_attend, menu);
    }
    */

    @Override
    public void createTopic(IReturnBool iReturnBool) {
        topicViewModel.createTheTopic(TopicType.PICK_UP, iReturnBool);
    }

    @Override
    public void popupMenu(@NonNull Context context, @NonNull View anchor) {

    }
}
