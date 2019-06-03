package com.thumbstage.hydrogen.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.app.Config;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.HyFile;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.bo.MessageType;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IReturnLine;
import com.thumbstage.hydrogen.model.callback.IReturnMic;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.callback.IReturnTopic;
import com.thumbstage.hydrogen.model.callback.IReturnUser;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.model.vo.Topic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.repository.FieldName;
import com.thumbstage.hydrogen.repository.TableName;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CloudAPI {

    final  String TAG = "CloudAPI";

    private final String APP_ID = "mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz";
    private final String APP_KEY = "o5CboiXK6ONj59aq0lMPJGS3";

    @Inject
    public CloudAPI(Context context) {
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
        checkInCurrentUser();
    }

    private  AVACL generateDefaultACL() {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        return acl;
    }

    public interface ICallBack {
        void callback(String objectID);
    }

    public interface IReturnUsers {
        void callback(List<User> users);
    }

    public interface IReturnURL {
        void callback(String url);
    }

    public interface IReturnFile {
        void callback(File file);
    }

    public void updateMicMembers(final Mic mic, final IReturnBool iReturnBool) {
        final String userId = AVUser.getCurrentUser().getObjectId();
        AVIMClient client = AVIMClient.getInstance(userId);
        AVIMConversation conversation = client.getConversation(mic.getId());
        List<String> friendsList = users2Ids(mic.getTopic().getMembers());
        conversation.addMembers(friendsList, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                updateTopicMembers(mic.getTopic(), iReturnBool);
            }
        });
    }

    private void createMic(final Topic topic, final ICallBack iCallBack) {
        String userId = AVUser.getCurrentUser().getObjectId();
        AVIMClient client = AVIMClient.getInstance(userId);
        client.createConversation(DataConvertUtil.user2StringId(topic.getMembers()), topic.getName(), null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(final AVIMConversation conversation, AVIMException e) {
                if(e == null) {
                    Log.i(TAG, "createMic ok");
                    AVObject avMic = AVObject.createWithoutData(TableName.TABLE_MIC.name, conversation.getConversationId());// 构建对象
                    AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
                    avMic.put(FieldName.FIELD_TOPIC.name, avTopic);
                    avMic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                iCallBack.callback(conversation.getConversationId());
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveFile(File file, final IReturnHyFile iReturnHyFile) {
        final AVFile avFile;
        try {
            avFile = AVFile.withAbsoluteLocalPath(file.getName(), file.getPath());
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e== null) {
                        HyFile hyFile = new HyFile(avFile.getName(), avFile.getObjectId(), avFile.getUrl(), true);
                        iReturnHyFile.callback(hyFile);
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkInCurrentUser() {
        if(AVUser.getCurrentUser() != null) {
            AVIMClient client = AVIMClient.getInstance(AVUser.getCurrentUser());
            client.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if(e == null) {

                    } else {
                        e.printStackTrace();
                        throw new IllegalStateException();
                    }
                }
            });
        }
    }

    private void checkOutCurrentUser() {
        if(AVUser.getCurrentUser() != null) {
            AVIMClient client = AVIMClient.getInstance(AVUser.getCurrentUser());
            client.close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if(e == null) {

                    } else {
                        e.printStackTrace();
                        throw new IllegalStateException();
                    }
                }
            });
        }
    }

    public void sendLine(final Mic mic, final Line line, final IReturnBool iReturnBool) {
        AVIMClient client = AVIMClient.getInstance(AVUser.getCurrentUser());
        AVIMConversation avMic = client.getConversation(mic.getId());
        final AVIMTypedMessage msg = line2Message(line);
        avMic.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if(e == null) {
                    // TODO: 5/13/2019 addTopicOneLine may remove to server sometime
                    if(msg instanceof AVIMAudioMessage) {
                        line.setWhat(((AVIMAudioMessage) msg).getFileUrl());
                    }
                    addTopicOneLine(mic.getTopic(), line, iReturnBool);
                } else {
                    iReturnBool.callback(false);
                }
            }
        });
    }

    public void updateMic(final Mic mic, final ICallBack iCallBack) {
        updateTopic(mic.getTopic(), new ICallBack() {
            @Override
            public void callback(String objectID) {
                AVObject avMic = AVObject.createWithoutData(TableName.TABLE_MIC.name, mic.getId());
                avMic.put(FieldName.FIELD_TOPIC.name, mic.getTopic());
                avMic.put("m", mic.getTopic().getMembers());
                avMic.put("name",mic.getTopic().getName());
                avMic.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null) {
                            iCallBack.callback(mic.getId());
                        }
                    }
                });
            }
        });
    }

    public void createMic(@NonNull final Mic mic, final ICallBack iCallBack) {
        createTopic(mic.getTopic(), new ICallBack() {
            @Override
            public void callback(final String topicID) {
                mic.getTopic().setId(topicID);
                createMic(mic.getTopic(), new ICallBack() {
                    @Override
                    public void callback(String micID) {
                        Log.i(TAG, "createTopic topicID:"+topicID+" micID:"+micID);
                        mic.setId(micID);
                        mic.getTopic().setId(topicID);
                        iCallBack.callback(micID);
                    }
                });
            }
        });
    }

    public void closeMic(@NonNull final Mic mic, final ICallBack iCallBack) {
        closeTopic(mic.getTopic(), new ICallBack() {
            @Override
            public void callback(String objectID) {
                if(!TextUtils.isEmpty(objectID)) {
                    AVObject avMic = AVObject.createWithoutData(TableName.TABLE_MIC.name, mic.getId());
                    avMic.put(FieldName.FIELD_MIC_MEMBERS.name, new ArrayList<>());
                    avMic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                iCallBack.callback(mic.getId());
                            } else {
                                iCallBack.callback(null);
                            }
                        }
                    });
                }
            }
        });
    }

    private void closeTopic(@NonNull final Topic topic, final ICallBack iCallBack) {
        AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        avTopic.put(FieldName.FIELD_IS_FINISHED.name, topic.isFinished());
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    iCallBack.callback(topic.getId());
                } else {
                    iCallBack.callback(null);
                }
            }
        });
    }

    /*
    private void updateTopicType(final Topic topic, final ICallBack iCallBack) {
        AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        avTopic.put(FieldName.FIELD_TAG.name, topic.getTags().name());
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    iCallBack.callback(topic.getId());
                } else {
                    iCallBack.callback(null);
                }
            }
        });
    }
    */

    public void updateTopicSetting(final Topic topic, final IReturnBool iReturnBool) {
        if(topic.getSetting()!= null) {
            final AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
            AVObject avObject = AVObject.createWithoutData(TableName.TABLE_FILE.name, topic.getSetting().getId());
            avTopic.put(FieldName.FIELD_SETTING.name, avObject);
            avTopic.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e == null) {
                        Log.i(TAG, "createTopic ok");
                        iReturnBool.callback(true);
                    } else {
                        iReturnBool.callback(false);
                    }
                }
            });
        } else {
            iReturnBool.callback(true);
        }
    }

    public void updateTopicInfo(final Topic topic, final IReturnBool iReturnBool) {
        final AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        avTopic.put(FieldName.FIELD_NAME.name, topic.getName());
        avTopic.put(FieldName.FIELD_BRIEF.name, topic.getBrief());
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    Log.i(TAG, "createTopic ok");
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
            }
        });
    }

    public void updateTopic(final Topic topic, final ICallBack iCallBack) {
        topic.setSponsor(getCurrentUser());
        if( getCurrentUser()!=null && !topic.getMembers().contains(getCurrentUser()) ) {
            topic.getMembers().add(getCurrentUser());
        }
        final AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        avTopic.put(FieldName.FIELD_NAME.name, topic.getName());
        avTopic.put(FieldName.FIELD_BRIEF.name, topic.getBrief());
        avTopic.put(FieldName.FIELD_TAG.name, convert2String(topic.getTags()));
        avTopic.put(FieldName.FIELD_SPONSOR.name, AVUser.getCurrentUser());
        if( !TextUtils.isEmpty( topic.getDerive_from() ) ) {
            AVObject avDeriveFrom = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getDerive_from());
            avTopic.put(FieldName.FIELD_DERIVE_FROM.name, avDeriveFrom);
        }
        if(topic.getSetting() != null) {
            AVObject avObject = AVObject.createWithoutData(TableName.TABLE_FILE.name, topic.getSetting().getId());
            avTopic.put(FieldName.FIELD_SETTING.name, avObject);
        }
        avTopic.put(FieldName.FIELD_MEMBER.name, DataConvertUtil.user2StringId(topic.getMembers()));
        List<Map> list = DataConvertUtil.convert2AVObject(topic.getDialogue());
        avTopic.put(FieldName.FIELD_DIALOGUE.name, list);
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    Log.i(TAG, "createTopic ok");
                    iCallBack.callback(avTopic.getObjectId());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createTopic(@NonNull final Topic topic, final ICallBack iCallBack) {
        User originUser = topic.getSponsor();
        topic.setSponsor(getCurrentUser());
        if( getCurrentUser()!=null && !topic.getMembers().contains(getCurrentUser()) ) {
            topic.getMembers().add(getCurrentUser());
        }
        final AVObject avTopic = new AVObject(TableName.TABLE_TOPIC.name);
        avTopic.put(FieldName.FIELD_NAME.name, topic.getName());
        avTopic.put(FieldName.FIELD_BRIEF.name, topic.getBrief());
        avTopic.put(FieldName.FIELD_TAG.name, convert2String(topic.getTags()));
        avTopic.put(FieldName.FIELD_SPONSOR.name, AVUser.getCurrentUser());
        if( !TextUtils.isEmpty( topic.getDerive_from() ) ) {
            AVObject avDeriveFrom = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getDerive_from());
            avTopic.put(FieldName.FIELD_DERIVE_FROM.name, avDeriveFrom);
        }
        if(topic.getSetting() != null) {
            AVObject avObject = AVObject.createWithoutData(TableName.TABLE_FILE.name, topic.getSetting().getId());
            avTopic.put(FieldName.FIELD_SETTING.name, avObject);
        }
        avTopic.put(FieldName.FIELD_MEMBER.name, DataConvertUtil.user2StringId(topic.getMembers()));

        // TODO: 5/13/2019 such privileges set must be removed later and move such function to server
        // region remove block *** default privilege for one member on topic
        if(avTopic.get(FieldName.FIELD_DERIVE_FROM.name) !=null ) {
            if(originUser != null) {
                String[] privileges = {CanOnTopic.CLOSE.name(), CanOnTopic.DELETE.name()};
                setMemberPrivilege2Topic(avTopic, originUser.getId(), privileges);
            }
        } else {
            String[] privileges = {CanOnTopic.CLOSE.name(), CanOnTopic.DELETE.name(),
                    CanOnTopic.PARTICIPANT.name(), CanOnTopic.SETUPINFO.name()};
            setMemberPrivilege2Topic(avTopic, getCurrentUserId(), privileges);
        }
        //endregion

        List<Map> list = DataConvertUtil.convert2AVObject(topic.getDialogue());
        avTopic.put(FieldName.FIELD_DIALOGUE.name, list);
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    Log.i(TAG, "createTopic ok");
                    iCallBack.callback(avTopic.getObjectId());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setMemberPrivilege2Topic(AVObject avTopic, String userId, String[] privileges) {
        Map data = convertPrivilegeOnTopic2AVObject(userId, privileges);
        avTopic.put(FieldName.FIELD_PRIVILEGE.name, data);
    }

    private Map convertPrivilegeOnTopic2AVObject(String userId, String[] privileges) {
        Map map = new HashMap();
        map.put(userId, privileges);
        return map;
    }

    public void copyTopic(Topic topic, final ICallBack iCallBack) {
        topic.setDerive_from(topic.getId());
        createTopic(topic, iCallBack);
    }

    private void updateTopicMembers(Topic topic,final IReturnBool iReturnBool) {
        AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        avTopic.put(FieldName.FIELD_MEMBER.name, users2Ids(topic.getMembers()));
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null) {
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
            }
        });

    }

    public void getMic(String micId, final IReturnMic iReturnMic) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_MIC.name);
        avQuery.include(FieldName.FIELD_TOPIC.name);
        avQuery.include(FieldName.FIELD_TOPIC.name+"."+FieldName.FIELD_SPONSOR.name);
        avQuery.getInBackground(micId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if(e == null) {
                    avObject2Mic(object, new IReturnMic() {
                        @Override
                        public void callback(Mic mic) {
                            iReturnMic.callback(mic);
                        }
                    });
                }
            }
        });
    }

    private void avObject2Mic(final AVObject avObject, final IReturnMic iReturnMic) {
        AVObject avTopic = avObject.getAVObject(FieldName.FIELD_TOPIC.name);
        getTopic(avTopic, new IReturnTopic() {
            @Override
            public void callback(Topic topic) {
                Mic mic = new Mic();
                mic.setId(avObject.getObjectId());
                mic.setTopic(topic);
                iReturnMic.callback(mic);
            }
        });
    }

    private List<Mic> convert2Mic(List<AVObject> avObjects) {
        List<Mic> mics = new ArrayList<>();
        for(AVObject avObject: avObjects) {
            Mic mic = new Mic();
            mic.setId(avObject.getObjectId());
            mic.setUpdateAt(avObject.getUpdatedAt());
            mic.setTopic(convert2TopicMini(avObject.getAVObject(FieldName.FIELD_TOPIC.name)));
            mics.add(mic);
        }
        return mics;
    }

    private Topic convert2TopicMini(AVObject avTopic) {
        String id = avTopic.getObjectId();
        String name = (String) avTopic.get(FieldName.FIELD_NAME.name);
        List<String> tags = avTopic.getList(FieldName.FIELD_TAG.name);
        String brief = (String) avTopic.get(FieldName.FIELD_BRIEF.name);
        boolean isFinished = avTopic.getBoolean(FieldName.FIELD_IS_FINISHED.name);
        Date lastRefresh = avTopic.getUpdatedAt();
        User user = convert2User(avTopic.getAVObject(FieldName.FIELD_SPONSOR.name));
        List<String> membersIds = avTopic.getList(FieldName.FIELD_MEMBER.name);

        Topic topic = new Topic();
        topic.setTags(convert2TopicTag(tags));
        topic.setId(id);
        topic.setName(name);
        topic.setBrief(brief);
        topic.setSponsor(user);
        topic.setMembers(convert2UserMini(membersIds));
        topic.setFinished(isFinished);
        topic.setUpdateAt(lastRefresh);
        return topic;
    }

    private List<User> convert2UserMini(List<String> userId) {
        List<User> users = new ArrayList<>();
        for(String id: userId) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        return users;
    }

    private User convert2User(AVObject avUser) {
        User user = new User();
        user.setId(avUser.getObjectId());
        user.setName((String) avUser.get(FieldName.FIELD_USERNAME.name));
        user.setAvatar((String) avUser.get(FieldName.FIELD_AVATAR.name));
        List<String> prilist = avUser.getList(FieldName.FIELD_PRIVILEGE.name);
        Set<Privilege> privileges = new LinkedHashSet<>();
        for(String str: prilist) {
            privileges.add(Privilege.valueOf(str));
        }
        user.setPrivileges(privileges);
        return user;
    }

    public void getMic(TopicTag tag, String userId, boolean isFinished, int pageNum, final IReturnMicList iReturnMicList) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_MIC.name);
        avQuery.include(FieldName.FIELD_TOPIC.name);
        avQuery.include(FieldName.FIELD_TOPIC.name+"."+FieldName.FIELD_SPONSOR.name);
        AVQuery<AVObject> avTopicQuery = new AVQuery<>(TableName.TABLE_TOPIC.name);
        if(!TextUtils.isEmpty(userId)) {
            avTopicQuery.whereEqualTo(FieldName.FIELD_MEMBER.name, userId);
        }
        avTopicQuery.whereEqualTo(FieldName.FIELD_TAG.name, tag.name());
        avTopicQuery.whereEqualTo(FieldName.FIELD_IS_FINISHED.name, isFinished);
        avQuery.whereMatchesQuery(FieldName.FIELD_TOPIC.name, avTopicQuery);
        avQuery.orderByDescending(FieldName.FIELD_UPDATE_AT.name);
        avQuery.limit(Config.PAGE_SIZE);
        avQuery.skip(pageNum * Config.PAGE_SIZE);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    iReturnMicList.callback(convert2Mic(avObjects));
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

    public void addContact(String whoId, String contactId, final IReturnBool iReturnBool) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_CONTACT.name);
        final AVObject avWho = AVObject.createWithoutData(TableName.TABLE_USER.name, whoId);
        final AVObject avContact = AVObject.createWithoutData(TableName.TABLE_USER.name, contactId);
        avQuery.whereEqualTo(FieldName.FIELD_WHO.name, avWho);
        avQuery.whereEqualTo(FieldName.FIELD_CONTACT.name, avContact);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null) {
                    if(avObjects.size() == 0) {
                        AVObject contact = new AVObject(TableName.TABLE_CONTACT.name);
                        contact.put(FieldName.FIELD_WHO.name, avWho);
                        contact.put(FieldName.FIELD_CONTACT.name, avContact);
                        contact.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e == null) {
                                    iReturnBool.callback(true);
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    public void getContact(String userId, int pageNum, final IReturnUsers iReturnUsers) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_CONTACT.name);
        avQuery.orderByDescending(FieldName.FIELD_CREATED_AT.name);
        avQuery.include(FieldName.FIELD_CONTACT.name);
        AVObject avWho = AVObject.createWithoutData(TableName.TABLE_USER.name, userId);
        avQuery.whereEqualTo(FieldName.FIELD_WHO.name, avWho);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e == null) {
                    List<AVObject> avUsers = new ArrayList<>();
                    for(AVObject avObject: avObjects) {
                        avUsers.add(avObject.getAVObject(FieldName.FIELD_CONTACT.name));
                    }
                    iReturnUsers.callback(avObject2User(avUsers));
                }
            }
        });
    }

    private List<String> users2Ids(List<User> users) {
        List<String> userIds = new ArrayList<>();
        for(User user :users) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    private void getTopic(final AVObject avTopic, final IReturnTopic iReturnTopic) {
        if(avTopic != null) {
            final AVFile avFile = avTopic.getAVFile(FieldName.FIELD_SETTING.name);
            final String id = avTopic.getObjectId();
            final String name = (String) avTopic.get(FieldName.FIELD_NAME.name);
            final List<String> tags = avTopic.getList(FieldName.FIELD_TAG.name);
            final String brief = (String) avTopic.get(FieldName.FIELD_BRIEF.name);
            final List<Map> datalist = avTopic.getList(FieldName.FIELD_DIALOGUE.name);
            final Map userCanMap = avTopic.getMap(FieldName.FIELD_PRIVILEGE.name);
            final List<String> membersIds = avTopic.getList(FieldName.FIELD_MEMBER.name);
            final boolean isFinished = avTopic.getBoolean(FieldName.FIELD_IS_FINISHED.name);
            getUsers(membersIds, new IReturnUsers() {
                @Override
                public void callback(List<User> users) {
                    List<Line> dialogue = DataConvertUtil.convert2Line(datalist, users);
                    AVObject avStartedBy = avTopic.getAVObject(FieldName.FIELD_SPONSOR.name);
                    User user = avObject2User(avStartedBy);
                    Setting setting;
                    if (avFile != null) {
                        boolean isInCloud = false;
                        if(!TextUtils.isEmpty(avFile.getBucket())) {
                            isInCloud = true;
                        }
                        setting = new Setting(avFile.getObjectId(), avFile.getUrl(), isInCloud);
                    } else {
                        setting = null;
                    }
                    getUsers(membersIds, new IReturnUsers() {
                        @Override
                        public void callback(List<User> users) {

                        }
                    });
                    Map<String, Set<CanOnTopic>> userCans = new HashMap<>();
                    if(userCanMap != null) {
                        for (Object key : userCanMap.keySet()) {
                            List<String> pris = new ArrayList<>();
                            if (userCanMap.get(key) instanceof JSONArray) {
                                JSONArray arr = (JSONArray) userCanMap.get(key);
                                pris = JSONObject.parseArray(arr.toJSONString(), String.class);
                            } else if (userCanMap.get(key) instanceof ArrayList) {
                                pris = (List<String>) userCanMap.get(key);
                            } else {

                            }
                            Set<CanOnTopic> cans = new LinkedHashSet<>();
                            for (String str : pris) {
                                cans.add(CanOnTopic.valueOf(str));
                            }
                            userCans.put((String) key, cans);
                        }
                    }
                    Date lastRefresh = avTopic.getUpdatedAt();

                    Topic topic = new Topic();
                    topic.setTags(convert2TopicTag(tags));
                    topic.setId(id);
                    topic.setBrief(brief);
                    topic.setName(name);
                    topic.setUserCan(userCans);
                    topic.setDialogue(dialogue);
                    topic.setMembers(users);
                    topic.setSponsor(user);
                    topic.setSetting(setting);
                    topic.setFinished(isFinished);
                    topic.setUpdateAt(lastRefresh);
                    iReturnTopic.callback(topic);
                }
            });
        }
    }

    private Set<TopicTag> convert2TopicTag(List<String> tags) {
        Set<TopicTag> list = new LinkedHashSet<>();
        for(String tag: tags) {
            list.add(TopicTag.valueOf(tag));
        }
        return list;
    }

    private List<String> convert2String(Set<TopicTag> tags) {
        List<String> list = new ArrayList<>();
        for(TopicTag tag: tags) {
            list.add(tag.name());
        }
        return list;
    }

    private void addTopicOneLine(Topic topic, final Line line, final IReturnBool callBack) {
        AVObject avTopic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, topic.getId());
        Map data = DataConvertUtil.convert2AVObject(line);
        avTopic.add(FieldName.FIELD_DIALOGUE.name, data);
        avTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    callBack.callback(true);
                } else {
                    callBack.callback(false);
                }
            }
        });
    }

    public void addTopicOneLine(final Mic mic, final Line line, final IReturnBool callBack) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_MIC.name);
        avQuery.getInBackground(mic.getId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if( e == null ) {
                    AVObject avTopic = avObject.getAVObject(FieldName.FIELD_TOPIC.name);
                    Map data = DataConvertUtil.convert2AVObject(line);
                    final AVObject topic = AVObject.createWithoutData(TableName.TABLE_TOPIC.name, avTopic.getObjectId());
                    topic.add(FieldName.FIELD_DIALOGUE.name, data);
                    topic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                callBack.callback(true);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getTopic(Mic mic, @NonNull final IReturnTopic returnTopic) {
        AVQuery<AVObject> avQuery = new AVQuery<>(TableName.TABLE_MIC.name);
        avQuery.include(FieldName.FIELD_TOPIC.name);
        avQuery.getInBackground(mic.getId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if( e == null ) {
                    AVObject avTopic = avObject.getAVObject(FieldName.FIELD_TOPIC.name);
                    getTopic(avTopic, new IReturnTopic() {
                        @Override
                        public void callback(Topic topic) {
                            returnTopic.callback(topic);
                        }
                    });
                } else {
                    // TODO: 3/1/2019 toast something wrong
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateUserAvatar(String avatar, final IReturnBool iReturnBool) {
        AVUser.getCurrentUser().put(FieldName.FIELD_AVATAR.name, avatar);
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    iReturnBool.callback(true);
                } else {
                    iReturnBool.callback(false);
                }
            }
        });
    }

    public void getUsers(List<String> membersId, final IReturnUsers iReturnUsers) {
        AVQuery<AVObject> query = new AVQuery<>(TableName.TABLE_USER.name);
        query.whereContainedIn(FieldName.FIELD_ID.name, membersId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    List<User> users = avObject2User(avObjects);
                    iReturnUsers.callback(users);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

    private AVIMTypedMessage line2Message(Line line) {
        AVIMTypedMessage message = null;
        switch (line.getMessageType()) {
            case TEXT: {
                message = new AVIMTextMessage();
                ((AVIMTextMessage) message).setText(line.getWhat());
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("type", line.getLineType().name());
                ((AVIMTextMessage) message).setAttrs(attributes);
            }
            break;
            case AUDIO:
                try {
                    AVFile file = AVFile.withAbsoluteLocalPath(getCurrentUserId()+".mp3",line.getWhat());
                    message = new AVIMAudioMessage(file);
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("type", line.getLineType().name());
                    ((AVIMAudioMessage)message).setAttrs(attributes);
                    // file.saveInBackground(); // if donot show in _File then save it manually
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        return message;
    }

    private Line message2Line(AVIMMessage message) {
        Line line = null;
        if(message instanceof AVIMTextMessage) {
            LineType type = LineType.LT_DIALOGUE;
            if(((AVIMTextMessage) message).getAttrs() != null && !TextUtils.isEmpty((String) ((AVIMTextMessage) message).getAttrs().get("type"))) {
                type = LineType.valueOf((String) ((AVIMTextMessage) message).getAttrs().get("type"));
            }
            line = new Line(new User(message.getFrom(), "",""), new Date(message.getTimestamp()), ((AVIMTextMessage) message).getText(), type);
        }
        return line;
    }

    public void getLastLineUser(Mic mic, final IReturnUser iReturnUser) {
        AVIMConversation conversation = getConversation(mic.getId());
        if(conversation != null) {
            AVIMMessage message = conversation.getLastMessage();
            if(message != null) {
                getUser(message.getFrom(), new IReturnUser() {
                    @Override
                    public void callback(User user) {
                        iReturnUser.callback(user);
                    }
                });
            } else {
                iReturnUser.callback(null);
            }
        } else {
            iReturnUser.callback(null);
        }

    }

    public void getLastLine(Mic mic, final IReturnLine iReturnLine) {
        AVIMConversation conversation = getConversation(mic.getId());
        if(conversation != null) {
            AVIMMessage message = conversation.getLastMessage();
            if(message != null) {
                iReturnLine.callback(message2Line(message));
            } else {
                iReturnLine.callback(null);
            }
        } else {
            iReturnLine.callback(null);
        }

    }

    public void saveResURL2Cloud(String URL, final IReturnHyFile iReturnHyFile) {
        final AVFile avFile = new AVFile(StringUtil.getSuffix(URL), URL, new HashMap<String, Object>() );
        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    HyFile hyFile = new HyFile(avFile.getName(), avFile.getObjectId(), avFile.getUrl(), false);
                    iReturnHyFile.callback(hyFile);
                }
            }
        });
    }

    public void getFileFromCloud(HyFile hyFile, final IReturnFile iReturnFile) {
        try {
            final AVFile avfile = AVFile.withObjectId(hyFile.getId());
            avfile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, AVException e) {
                    if( e==null ) {
                        File file = new File(avfile.getName());
                        try {
                            OutputStream output = new FileOutputStream(file);
                            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                            bufferedOutput.write(data);
                            iReturnFile.callback(file);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
        } catch (AVException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private AVIMClient getClient() {
        AVUser avUser = AVUser.getCurrentUser();
        if (!TextUtils.isEmpty(avUser.getObjectId()) && avUser != null) {
            return AVIMClient.getInstance(avUser.getObjectId());
        }
        return null;
    }

    private AVIMConversation getConversation(final String conversationId) {
        return getClient().getConversation(conversationId);
    }

    private String getCurrentUserId() {
        AVUser avUser = AVUser.getCurrentUser();
        if(avUser != null) {
            return avUser.getObjectId();
        }
        return null;
    }

    public void signOut() {
        checkOutCurrentUser();
        AVUser.logOut();
    }

    public void signUp(String name, String password, String email, final IReturnUser iReturnUser) {
        final AVUser avUser = new AVUser();
        avUser.setUsername( name );
        avUser.setPassword( password );
        avUser.setEmail(email);
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if( e == null ) {
                    User user = avUser2User(avUser);
                    iReturnUser.callback(user);
                } else {
                    switch (e.getCode()) {
                        case 202: {

                        }
                        break;
                        case 203: {

                        }
                        break;
                        case 214: {

                        }
                        break;
                    }
                }
            }
        });
    }

    public void signIn(String id, String password, final IReturnUser iReturnUser) {
        AVUser.logInInBackground(id, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avObject, AVException e) {
                if(e == null) {
                    User user = avObject2User(avObject);
                    checkInCurrentUser();
                    iReturnUser.callback(user);
                }
            }
        });
    }

    public void getUser(String userID, final IReturnUser iReturnUser) {
        AVObject userObject = AVObject.createWithoutData(TableName.TABLE_USER.name, userID);
        userObject.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null) {
                    User user = avObject2User(avObject);
                    iReturnUser.callback(user);
                }
            }
        });
    }

    private User defaultUser = new User(StringUtil.DEFAULT_USERID, StringUtil.DEFAULT_USERID, "");

    public User getCurrentUser() {
        User user = avUser2User(AVUser.getCurrentUser());
        return user != null? user : defaultUser;
    }

    private User avUser2User(AVUser avUser) {
        if(avUser!=null) {
            User user = new User(avUser.getObjectId(), avUser.getUsername(), (String) avUser.get(FieldName.FIELD_AVATAR.name));
            List<String> prilist = avUser.getList(FieldName.FIELD_PRIVILEGE.name);
            Set<Privilege> privileges = new LinkedHashSet<>();
            if(prilist != null) {
                for (String str : prilist) {
                    privileges.add(Privilege.valueOf(str));
                }
            } else {
                privileges = new LinkedHashSet<Privilege>() {
                    {
                        add(Privilege.BROWSE_COMMUNITY_SHOW);
                    }
                };
            }
            user.setPrivileges(privileges);
            return user;
        }
        return null;
    }

    private List<User> avObject2User(List<AVObject> list) {
        List<User> users = new ArrayList<>();
        for(AVObject avObject: list) {
            User user = avObject2User(avObject);
            users.add(user);
        }
        return users;
    }

    private User avObject2User(AVObject avObject) {
        if(avObject != null) {
            User user = new User(avObject.getObjectId(), (String) avObject.get(FieldName.FIELD_USERNAME.name), (String) avObject.get(FieldName.FIELD_AVATAR.name));
            List<String> prilist = avObject.getList(FieldName.FIELD_PRIVILEGE.name);
            Set<Privilege> privileges = new LinkedHashSet<>();
            for(String str: prilist) {
                privileges.add(Privilege.valueOf(str));
            }
            user.setPrivileges(privileges);
            return user;
        } else {
            return null;
        }
    }
}
