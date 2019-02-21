package com.thumbstage.hydrogen.utils;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.thumbstage.hydrogen.model.Topic;

import java.util.List;
import java.util.Map;


public class LCDBUtil {

    final static String TAG = "LCDBUtil";

    private static AVACL generateDefaultACL() {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        return acl;
    }

    public interface ICallBack {
        void callback(String objectID);
    }

    public static void saveIStartedOpenedTopic(final Topic topic) {
        saveTopic(topic, new ICallBack() {
            @Override
            public void callback(final String topicID) {
                createConversation(topic, new ICallBack() {
                    @Override
                    public void callback(String conversationID) {
                        Log.i(TAG, "saveIStartedOpenedTopic topicID:"+topicID+" conversationID:"+conversationID);
                        AVObject avTopic = AVObject.createWithoutData("Topic", topicID);
                        AVObject avConversation = AVObject.createWithoutData("_Conversation", conversationID);
                        avConversation.put("topic", avTopic);
                        avConversation.saveInBackground();
                        AVObject record = new AVObject("IStartedOpened");
                        record.put("topic", avTopic);
                        record.put("conversation", avConversation);
                        record.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                // TODO: 2/18/2019 need toast here
                                Log.i(TAG, "saveIStartedOpenedTopic ok");
                            }
                        });
                    }
                });
            }
        });
    }

    public static void savePublishedOpenedTopic(Topic topic, final ICallBack iCallBack) {
        saveTopic(topic, new ICallBack() {
            @Override
            public void callback(final String objectID) {
                Log.i(TAG, "savePublishedOpenedTopic id:"+objectID);
                AVObject avTopic = AVObject.createWithoutData("Topic", objectID);
                AVObject record = new AVObject("PublishedOpened");
                record.put("topic", avTopic);
                record.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        // TODO: 2/18/2019 need toast here
                        iCallBack.callback(objectID);
                        Log.i(TAG, "savePublishedOpenedTopic ok");
                    }
                });
            }
        });
    }

    public static void copyPublishedOpenedTopic(final Topic topic, final ICallBack iCallBack) {
        copyTopic(topic, new ICallBack() {
            @Override
            public void callback(final String topicID) {
                createConversation(topic, new ICallBack() {
                    @Override
                    public void callback(String conversationID) {
                        Log.i(TAG, "saveIStartedOpenedTopic topicID:"+topicID+" conversationID:"+conversationID);
                        AVObject avTopic = AVObject.createWithoutData("Topic", topicID);
                        AVObject avConversation = AVObject.createWithoutData("_Conversation", conversationID);
                        avConversation.put("topic", avTopic);
                        avConversation.saveInBackground();
                        AVObject record = new AVObject("IAttendedOpened");
                        record.put("topic", avTopic);
                        record.put("conversation", avConversation);
                        record.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                // TODO: 2/18/2019 need toast here
                                Log.i(TAG, "copyPublishedOpenedTopic ok");
                            }
                        });
                        iCallBack.callback(conversationID);
                    }
                });
            }
        });
    }

    public static void getConversation(String conversationID) {

    }

    public static void createConversation(final Topic topic, final ICallBack iCallBack) {
        AVIMClient client = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if(e == null) {
                    Log.i(TAG, "client open ok");
                    client.createConversation(topic.getMembers(), topic.getName(), null, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVIMException e) {
                            if(e == null) {
                                Log.i(TAG, "createConversation ok");
                                iCallBack.callback(conversation.getConversationId());
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void saveTopic(Topic topic, final ICallBack iCallBack) {
        final AVObject record = new AVObject("Topic");
        record.put("name", topic.getName());
        record.put("brief", topic.getBrief());
        record.put("started_by", AVUser.getCurrentUser());
        if( !TextUtils.isEmpty( topic.getDerive_from() ) ) {
            AVObject avDeriveFrom = AVObject.createWithoutData("Topic", topic.getDerive_from());
            record.put("derive_from", avDeriveFrom);
        }
        AVObject avObject = AVObject.createWithoutData("_File", topic.getSetting().getId());
        record.put("setting", avObject);
        record.put("members", topic.getMembers());
        List<Map> list = DataConvertUtil.convert2AVObject(topic.getDialogue());
        record.put("dialogue", list);
        record.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    Log.i(TAG, "saveTopic ok");
                    iCallBack.callback(record.getObjectId());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void copyTopic(Topic topic, final ICallBack iCallBack) {
        topic.setDerive_from(topic.getId());
        saveTopic(topic, iCallBack);
    }

}
