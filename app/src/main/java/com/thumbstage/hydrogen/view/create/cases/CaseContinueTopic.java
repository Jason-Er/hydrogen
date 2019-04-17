package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;

public class CaseContinueTopic extends CaseBase implements ICanPopupMenu {

    /*
    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_continue, menu);
    }
    */

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
    public void popupMenu(@NonNull Context context, @NonNull View anchor) {

    }
}
