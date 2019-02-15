package com.thumbstage.hydrogen.utils;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.List;

public class ClassDBUtil {

    private static AVACL generateDefaultACL() {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        return acl;
    };

    public static void createTopic(String className, AVUser startedBy, List<String> dialogue) {
        AVObject iStartedOpened = new AVObject( className );
        iStartedOpened.put("started_by", startedBy.getObjectId());
        iStartedOpened.put("dialogue", dialogue);
        iStartedOpened.setACL(generateDefaultACL());
        iStartedOpened.saveInBackground();
    };

}
