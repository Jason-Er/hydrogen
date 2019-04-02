package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IStatusCallBack;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;

public class CaseAttendTopic extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_attend, menu);
    }

    // region implements interface ICreateMenuItemFunction
    @Override
    public void sign(Context context) {
        Navigation.sign2Account(context);
    }

    @Override
    public void settings(Fragment fragment) {

    }

    @Override
    public void createTopic(IStatusCallBack iStatusCallBack) {
        topicViewModel.createTheTopic(TopicType.PICK_UP, iStatusCallBack);
    }

    @Override
    public void publishTopic(IStatusCallBack iStatusCallBack) {

    }
    //endregion
}
