package com.thumbstage.hydrogen.data;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LCRepository {

    final static String TAG = "LCRepository";

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
        if(topic.getSetting() != null) {
            AVObject avObject = AVObject.createWithoutData("_File", topic.getSetting().getId());
            record.put("setting", avObject);
        }
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

    public interface ITopicExCallBack {
        void callback(List<TopicEx> topicExList);
    }

    public enum TopicExType {
        PUBLISHED_OPENED("PublishedOpened"), ISTARTED_OPENED("IStartedOpened"), IATTENDED_OPENED("IAttendedOpened");
        final String title;
        TopicExType(String title) {
            this.title = title;
        }
    }

    public static void getTopicEx(TopicExType type, int pageNum, final ITopicExCallBack callBack) {
        AVQuery<AVObject> avQuery = new AVQuery<>(type.title);
        avQuery.include("topic");
        avQuery.orderByAscending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                List<TopicEx> topicExes = new ArrayList<>();
                if(avException == null) {
                    for(AVObject avObject: avObjects) {
                        Log.i("BrowseViewModel", "OK");
                        AVObject avTopic = avObject.getAVObject("topic");
                        if(avTopic != null) {
                            AVFile avFile = avTopic.getAVFile("setting");
                            String id = avTopic.getObjectId();
                            String name = (String) avTopic.get("name");
                            String brief = (String) avTopic.get("brief");
                            List<Map> datalist = avTopic.getList("dialogue");
                            List<String> members = avTopic.getList("members");
                            List<Line> dialogue = new ArrayList<>();
                            for (Map map : datalist) {
                                if( map.size() != 0 ) {
                                    dialogue.add(new Line(
                                            (String) map.get("who"),
                                            StringUtil.string2Date((String) map.get("when")),
                                            (String) map.get("what"),
                                            (LineType.valueOf((String) map.get("type")))));
                                }
                            }
                            AVObject avPipe = avObject.getAVObject("conversation");
                            AVObject avStartedBy = avTopic.getAVObject("started_by");
                            User user = new User(avStartedBy.getObjectId(), (String) avStartedBy.get("name"), (String) avStartedBy.get("avatar"));
                            Setting setting;
                            if(avFile != null) {
                                setting = new Setting(avFile.getObjectId(), avFile.getUrl());
                            } else {
                                setting = null;
                            }
                            Topic topic = Topic.Builder()
                                    .setId(id)
                                    .setBrief(brief)
                                    .setName(name)
                                    .setDialogue(dialogue)
                                    .setMembers(members)
                                    .setStarted_by(user)
                                    .setSetting(setting);
                            Pipe pipe;
                            if( avPipe != null ) {
                                pipe = new Pipe(avPipe.getObjectId());
                            } else {
                                pipe = null;
                            }
                            TopicEx topicEx = new TopicEx(topic, pipe);
                            topicExes.add(topicEx);
                        }
                    }
                    callBack.callback(topicExes);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

}
