package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.HyFile;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IStatusCallBack;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.ICreateCustomize;
import com.thumbstage.hydrogen.view.create.ICreateMenuItemFunction;
import com.thumbstage.hydrogen.view.create.TopicSettingDialog;

import java.io.File;

public class CaseCreateTopic extends CaseBase implements ICreateMenuItemFunction, ICreateCustomize {

    final String TAG = "CaseCreateTopic";
    Uri imageUri;

    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_create, menu);
    }

    // region implements interface ICreateMenuItemFunction
    @Override
    public void sign(Context context) {
        Navigation.sign2Account(context);
    }

    @Override
    public void settings(final Fragment fragment) {

        TopicSettingDialog bottomDialog = new TopicSettingDialog();
        bottomDialog.setIOnOK(new TopicSettingDialog.IOnOK() {
            @Override
            public void callback(TopicSettingDialog.LocalData localData) {
                if(!TextUtils.isEmpty(localData.getName())) {
                    topicAdapter.getTopic().setName(localData.getName());
                }
                if(!TextUtils.isEmpty(localData.getBrief())) {
                    topicAdapter.getTopic().setBrief(localData.getBrief());
                }
                imageUri = localData.getImageUri();
                Glide.with(backgroundView).load(imageUri).into(backgroundView);
            }
        });
        bottomDialog.show(fragment.getFragmentManager(), "hello");

    }

    @Override
    public void createTopic(final IStatusCallBack iStatusCallBack) {
        Log.i(TAG, "createTopic");
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iStatusCallBack);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iStatusCallBack);
        }
    }

    @Override
    public void publishTopic(final IStatusCallBack iStatusCallBack) {
        Log.i(TAG, "publishTopic");
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.PUBLISHED, iStatusCallBack);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.PUBLISHED, iStatusCallBack);
        }
    }
    // endregion
}
