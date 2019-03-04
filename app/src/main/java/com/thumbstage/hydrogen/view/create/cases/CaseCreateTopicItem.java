package com.thumbstage.hydrogen.view.create.cases;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;

public class CaseCreateTopicItem extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    final String TAG = "CaseCreateTopicItem";

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine2Adapter((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_create, menu);
    }

    // region implements interface ICreateMenuItemFunction
    @Override
    public void sign(Context context) {
        Navigation.sign(context);
    }

    @Override
    public void settings(Context context) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_create_settings, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
        params.bottomMargin = DensityUtil.dp2px(context, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    @Override
    public void startTopic() {
        Log.i(TAG, "startTopic");
        if( topic.getMembers().size() == 0 ) {
            topic.getMembers().add(UserGlobal.getInstance().getCurrentUserId());
        }
        LCRepository.saveIStartedOpenedTopic(topic);
    }

    @Override
    public void publishTopic() {
        Log.i(TAG, "publishTopic");
        if( topic.getMembers().size() == 0 ) {
            topic.getMembers().add(UserGlobal.getInstance().getCurrentUserId());
        }
        LCRepository.savePublishedOpenedTopic(topic, new LCRepository.ICallBack() {
            @Override
            public void callback(String objectID) {

            }
        });
    }
    // endregion
}
