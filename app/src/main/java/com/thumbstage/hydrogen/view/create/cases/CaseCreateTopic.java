package com.thumbstage.hydrogen.view.create.cases;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.HyFile;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.view.create.TopicSettingDialog;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanPopupMenu;
import com.thumbstage.hydrogen.view.create.feature.ICanPublishTopic;
import com.thumbstage.hydrogen.view.create.feature.ICanSetSetting;

import java.io.File;
import java.lang.reflect.Field;

public class CaseCreateTopic extends CaseBase implements ICanCreateTopic,
        ICanPublishTopic, ICanSetSetting, ICanPopupMenu {

    final String TAG = "CaseCreateTopic";
    Uri imageUri;

    @Override
    public void setSetting(final Fragment fragment) {

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
    public void createTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "createTopic");
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iReturnBool);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.UNPUBLISHED, iReturnBool);
        }
    }

    @Override
    public void publishTopic(final IReturnBool iReturnBool) {
        Log.i(TAG, "publishTopic");
        if(imageUri != null) {
            File file = new File(imageUri.getPath());
            topicViewModel.saveFile(file, new IReturnHyFile() {
                @Override
                public void callback(HyFile hyFile) {
                    topicAdapter.getTopic().setSetting(new Setting(hyFile.getId(), hyFile.getUrl(), hyFile.getInCloud()));
                    topicViewModel.createTheTopic(TopicType.PUBLISHED, iReturnBool);
                }
            });
        } else {
            topicViewModel.createTheTopic(TopicType.PUBLISHED, iReturnBool);
        }
    }

    @Override
    public void popupMenu(@NonNull Context context, @NonNull View anchor) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_case_create, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_setting:
                        Log.i(TAG, "menu_item_setting");
                        break;
                    case R.id.menu_item_start:
                        Log.i(TAG, "menu_item_start");
                        break;
                    case R.id.menu_item_publish:
                        Log.i(TAG, "menu_item_publish");
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
