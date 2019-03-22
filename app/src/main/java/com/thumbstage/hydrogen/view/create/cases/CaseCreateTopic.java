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
import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.model.HyFile;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.model.Setting;
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
        Navigation.sign(context);
    }

    @Override
    public void settings(final Fragment fragment) {

        /*
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
                if(!TextUtils.isEmpty(localData.getImageURL())) {
                    topic.setSetting(new Setting("", localData.getImageURL(), false));
                }
                imageUri = localData.getImageUri();
                Glide.with(backgroundView).load(imageUri).into(backgroundView);
            }
        });
        bottomDialog.show(fragment.getFragmentManager(), "hello");
        */

    }

    @Override
    public void startTopic() {
        Log.i(TAG, "startTopic");
        viewModel.startTheTopic();
        /*
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            CloudAPI.saveFile2Cloud(file, new CloudAPI.IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topic.setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    CloudAPI.saveTopic2StartedOpened(topic);
                }
            });
        } else {
            CloudAPI.saveResURL2Cloud(topic.getSetting().getUrl(), new CloudAPI.IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topic.setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    CloudAPI.saveTopic2StartedOpened(topic);
                }
            });

        }
        */
    }

    @Override
    public void publishTopic() {
        Log.i(TAG, "publishTopic");
        viewModel.publishTheTopic();
        /*
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            CloudAPI.saveFile2Cloud(file, new CloudAPI.IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topic.setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    CloudAPI.saveTopic2PublishedOpened(topic, new CloudAPI.ICallBack() {
                        @Override
                        public void callback(String objectID) {

                        }
                    });
                }
            });
        } else {
            CloudAPI.saveResURL2Cloud(topic.getSetting().getUrl(), new CloudAPI.IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topic.setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    CloudAPI.saveTopic2PublishedOpened(topic, new CloudAPI.ICallBack() {
                        @Override
                        public void callback(String objectID) {

                        }
                    });
                }
            });
        }
        */
    }
    // endregion
}
