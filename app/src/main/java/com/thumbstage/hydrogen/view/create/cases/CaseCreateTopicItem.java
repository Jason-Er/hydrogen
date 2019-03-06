package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.common.FitCenterDrawable;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;
import com.thumbstage.hydrogen.view.create.TopicSettingDialog;

public class CaseCreateTopicItem extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    final String TAG = "CaseCreateTopicItem";
    Uri imageUri;

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
    public void settings(final Fragment fragment) {
        TopicSettingDialog bottomDialog = new TopicSettingDialog(); //context, R.style.BottomDialog
        bottomDialog.setIOnOK(new TopicSettingDialog.IOnOK() {
            @Override
            public void callback(TopicSettingDialog.LocalData localData) {
                if(!TextUtils.isEmpty(localData.getName())) {
                    topic.setName(localData.getName());
                }
                if(!TextUtils.isEmpty(localData.getBrief())) {
                    topic.setBrief(localData.getBrief());
                }
                imageUri = localData.getImageUri();
                Glide.with(fragment.getContext()).load(imageUri).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        fragment.getActivity().getWindow().setBackgroundDrawable(new FitCenterDrawable(resource));
                    }
                });
            }
        });
        bottomDialog.show(fragment.getFragmentManager(), "hello");
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
