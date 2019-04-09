package com.thumbstage.hydrogen.view.create.cases;

import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateOptionsMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;

public class CaseCopyTopic extends CaseBase implements ICanCreateTopic, ICanCreateOptionsMenu {

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_attend, menu);
    }

    @Override
    public void createTopic(IReturnBool iReturnBool) {
        topicViewModel.createTheTopic(TopicType.PICK_UP, iReturnBool);
    }

}
