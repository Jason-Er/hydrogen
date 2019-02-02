package com.thumbstage.hydrogen.utils;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;

public class ClassDBUtil {

    private static AVACL generateDefaultACL() {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        return acl;
    };

    public static void saveConversationRecord(String className, AVIMConversation conversation){
        AVObject iStartedOpened = new AVObject( className );
        iStartedOpened.put("started_by", conversation.getCreator());
        iStartedOpened.put("conversation", AVObject.createWithoutData("_Conversation", conversation.getConversationId()));
        iStartedOpened.setACL(generateDefaultACL());
        iStartedOpened.saveInBackground();
    };

}
