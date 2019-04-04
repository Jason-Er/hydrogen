package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;

public class CaseContinueTopic extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_default, menu);
    }

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

    // region implements interface ICreateMenuItemFunction
    @Override
    public void sign(Context context) {
        Navigation.sign2Account(context);
    }

    @Override
    public void settings(Fragment fragment) {

    }

    @Override
    public void createTopic(IReturnBool iReturnBool) {

    }

    @Override
    public void publishTopic(IReturnBool iReturnBool) {

    }
    //endregion
}
