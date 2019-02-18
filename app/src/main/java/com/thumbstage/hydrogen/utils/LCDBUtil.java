package com.thumbstage.hydrogen.utils;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LCDBUtil {

    final static String TAG = "LCDBUtil";

    private static AVACL generateDefaultACL() {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        return acl;
    };

    public static void saveIStartedOpenedTopic(Topic topic) {
        AVObject iStartedOpened = new AVObject("IStartedOpened");
        iStartedOpened.put("started_by", AVUser.getCurrentUser());
        iStartedOpened.put("name", topic.getName());
        iStartedOpened.put("brief", topic.getBrief());
        AVObject avObject = AVObject.createWithoutData("_File", topic.getSetting_id());
        iStartedOpened.put("setting", avObject);
        iStartedOpened.put("members", topic.getMembers());
        List<Map> list = DataConvertUtil.convert2AVObject(topic.getDialogue());
        iStartedOpened.put("dialogue", list);
        // iStartedOpened.setACL(generateDefaultACL());
        iStartedOpened.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                // TODO: 2/18/2019 need toast here
                Log.i(TAG, "saveIStartedOpenedTopic ok");
            }
        });
    };

    public static void savePublishedOpenedTopic(Topic topic) {
        AVObject publishedOpened = new AVObject("PublishedOpened");
        publishedOpened.put("started_by", AVUser.getCurrentUser().getObjectId());
        publishedOpened.put("name", topic.getName());
        publishedOpened.put("brief", topic.getBrief());
        publishedOpened.put("setting", topic.getSetting_id());
        publishedOpened.put("members", topic.getMembers());
        List<Map> list = DataConvertUtil.convert2AVObject(topic.getDialogue());
        publishedOpened.put("dialogue", list);
        publishedOpened.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                // TODO: 2/18/2019 need toast here
                Log.i(TAG, "savePublishedOpenedTopic ok");
            }
        });

    }





}
