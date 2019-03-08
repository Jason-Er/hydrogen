package com.thumbstage.hydrogen.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.HyFile;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.User;
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

    public interface IReturnBool {
        void callback(Boolean isOK);
    }

    public interface IReturnLine {
        void callback(Line line);
    }

    public interface IReturnTopic {
        void callback(Topic topic);
    }

    public interface IReturnPipe {
        void callback(Pipe pipe);
    }

    public interface IReturnUser {
        void callback(User user);
    }

    public interface IReturnURL {
        void callback(String url);
    }

    public interface IReturnHyFile {
        void callback(HyFile hyFile);
    }

    public interface IReturnFile {
        void callback(File file);
    }

    public static void saveIStartedOpenedTopic(final Topic topic) {
        saveTopic(topic, new ICallBack() {
            @Override
            public void callback(final String topicID) {
                createConversation(topic, new ICallBack() {
                    @Override
                    public void callback(String conversationID) {
                        Log.i(TAG, "saveIStartedOpenedTopic topicID:"+topicID+" conversationID:"+conversationID);
                        AVObject avTopic = AVObject.createWithoutData(Topic.class.getSimpleName(), topicID);
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
                AVObject avTopic = AVObject.createWithoutData(Topic.class.getSimpleName(), objectID);
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

    public static void copyPublishedOpenedTopic(final Topic topic, final IReturnPipe iCallBack) {
        copyTopic(topic, new ICallBack() {
            @Override
            public void callback(final String topicID) {
                createConversation(topic, new ICallBack() {
                    @Override
                    public void callback(String conversationID) {
                        Log.i(TAG, "saveIStartedOpenedTopic topicID:"+topicID+" conversationID:"+conversationID);
                        AVObject avTopic = AVObject.createWithoutData(Topic.class.getSimpleName(), topicID);
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
                        iCallBack.callback(new Pipe(conversationID));
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

    public static void saveTopic(@NonNull Topic topic, final ICallBack iCallBack) {
        if( topic.getMembers().size() == 0 ) {
            topic.getMembers().add(UserGlobal.getInstance().getCurrentUserId());
        }
        final AVObject record = new AVObject(Topic.class.getSimpleName());
        record.put("name", topic.getName());
        record.put("brief", topic.getBrief());
        record.put("started_by", AVUser.getCurrentUser());
        if( !TextUtils.isEmpty( topic.getDerive_from() ) ) {
            AVObject avDeriveFrom = AVObject.createWithoutData(Topic.class.getSimpleName(), topic.getDerive_from());
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
        avQuery.include("topic.started_by");
        avQuery.orderByAscending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                List<TopicEx> topicExes = new ArrayList<>();
                if(avException == null) {
                    for(AVObject avObject: avObjects) {
                        Log.i("BrowseViewModel", "OK");
                        AVObject avTopic = avObject.getAVObject("topic");
                        AVObject avPipe = avObject.getAVObject("conversation");
                        Pipe pipe;
                        if( avPipe != null ) {
                            pipe = new Pipe(avPipe.getObjectId());
                        } else {
                            pipe = null;
                        }
                        Topic topic = getTopic(avTopic);
                        TopicEx topicEx = new TopicEx(topic, pipe);
                        topicExes.add(topicEx);
                    }
                    callBack.callback(topicExes);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

    private static Topic getTopic(AVObject avTopic) {
        if(avTopic != null) {
            AVFile avFile = avTopic.getAVFile("setting");
            String id = avTopic.getObjectId();
            String name = (String) avTopic.get("name");
            String brief = (String) avTopic.get("brief");
            List<Map> datalist = avTopic.getList("dialogue");
            List<String> members = avTopic.getList("members");
            List<Line> dialogue = new ArrayList<>();
            for (Map map : datalist) {
                if (map.size() != 0) {
                    dialogue.add(new Line(
                            (String) map.get("who"),
                            StringUtil.string2Date((String) map.get("when")),
                            (String) map.get("what"),
                            (LineType.valueOf((String) map.get("type")))));
                }
            }
            AVObject avStartedBy = avTopic.getAVObject("started_by");
            User user = new User(avStartedBy.getObjectId(), (String) avStartedBy.get("username"), (String) avStartedBy.get("avatar"));
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
            Topic topic = Topic.Builder()
                    .setId(id)
                    .setBrief(brief)
                    .setName(name)
                    .setDialogue(dialogue)
                    .setMembers(members)
                    .setStarted_by(user)
                    .setSetting(setting);
            return topic;
        } else {
            return null;
        }
    }

    public static void addTopicOneLine(final Pipe pipe, final Line line, final IReturnBool callBack) {
        AVQuery<AVObject> avQuery = new AVQuery<>("_Conversation");
        avQuery.getInBackground(pipe.getId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if( e == null ) {
                    AVObject avTopic = avObject.getAVObject("topic");
                    Map data = DataConvertUtil.convert2AVObject(line);
                    final AVObject topic = AVObject.createWithoutData(Topic.class.getSimpleName(), avTopic.getObjectId());
                    topic.addUnique("dialogue", data);
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

    public static void getTopic(Pipe pipe, @NonNull final IReturnTopic returnTopic) {
        AVQuery<AVObject> avQuery = new AVQuery<>("_Conversation");
        avQuery.include("topic");
        avQuery.getInBackground(pipe.getId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if( e == null ) {
                    AVObject avTopic = avObject.getAVObject("topic");
                    Topic topic = getTopic(avTopic);
                    returnTopic.callback(topic);
                } else {
                    // TODO: 3/1/2019 toast something wrong
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getUser(String userID, final IReturnUser iReturnUser) {
        AVObject userObject = AVObject.createWithoutData("_User", userID);
        userObject.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null) {
                    User user = new User(avObject.getObjectId(), (String) avObject.get("username"), (String) avObject.get("avatar"));
                    iReturnUser.callback(user);
                }
            }
        });
    }

    public static void sendLine(final Pipe pipe, final Line line, final IReturnBool icallback) {
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(pipe.getId());
        if( !StringUtil.isUrl(line.getWhat()) ) { // must be a text
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(line.getWhat());
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("type", line.getLineType().name());
            message.setAttrs(attributes);

            AVIMMessageOption option = new AVIMMessageOption();
            option.setReceipt(true);

            conversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if( e == null ) {
                        addTopicOneLine(pipe, line, icallback); // TODO: 2/28/2019 may this function add to server
                    } else {
                        icallback.callback(false);
                    }
                }
            });
        }
    }

    private static Line message2Line(AVIMMessage message) {
        Line line = null;
        if(message instanceof AVIMTextMessage) {
            LineType type = LineType.LT_DIALOGUE;
            if(((AVIMTextMessage) message).getAttrs() != null && !TextUtils.isEmpty((String) ((AVIMTextMessage) message).getAttrs().get("type"))) {
                type = LineType.valueOf((String) ((AVIMTextMessage) message).getAttrs().get("type"));
            }
            line = new Line(message.getFrom(), new Date(message.getTimestamp()), ((AVIMTextMessage) message).getText(), type);
        }
        return line;
    }

    public static void getLastLineUser(Pipe pipe, final IReturnUser iReturnUser) {
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(pipe.getId());
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

    public static void getLastLine(Pipe pipe, final IReturnLine iReturnLine) {
        AVIMConversation conversation = UserGlobal.getInstance().getConversation(pipe.getId());
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

    public static void saveResURL2Cloud(String URL, final IReturnHyFile iReturnHyFile) {
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

    public static void saveFile2Cloud(final File file, final IReturnHyFile iReturnHyFile) {
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

    public static void getFileFromCloud(HyFile hyFile, final IReturnFile iReturnFile) {
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

}
