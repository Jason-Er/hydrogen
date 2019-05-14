package com.thumbstage.hydrogen;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.utils.StringUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void testBefore() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AVOSCloud.initialize(appContext,"mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz",
                "o5CboiXK6ONj59aq0lMPJGS3");
        AVOSCloud.setDebugLogEnabled(true);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.thumbstage.hydrogen", appContext.getPackageName());
    }

    @Test
    public void fetchLCConversation() {
        final List<String> convIdList = new ArrayList<>();
        AVQuery<AVObject> avQuery = new AVQuery<>("IStartedOpened");
        avQuery.orderByDescending("createdAt");
        // avQuery.include("conversation");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    Log.i("fetchLCConversation", "fetch back size():" + avObjects.size());
                    for(AVObject avObject: avObjects) {
                        Log.i("fetchLCConversation", avObject.toString());
                        Log.i("fetchLCConversation", "name: ");
                        //convIdList.add(id);
                    }

                } else {
                    Log.i("fetchLCConversation", "Wrong: "+ avException.getMessage());
                }
            }
        });
    }

    class Line {
        String who;
        String say;
        Date date;
        public Line(String who, String say, Date date) {
            this.who = who;
            this.say = say;
            this.date = date;
        }
    }

    @Test
    public void saveList2LeanCloud() {

        ArrayList<Map> dialogue = new ArrayList<>();
        // dialogue.add(new Line(AVUser.getCurrentUser().getObjectId(),"hello", new Date()));
        Map<String,Object> map=new HashMap<>();
        map.put("who", AVUser.getCurrentUser().getObjectId());
        map.put("say","hello");
        map.put("when",new Date());
        dialogue.add(map);
        // dialogue.add("hello");
        // dialogue.add("http://img.mp.itc.cn/upload/20170315/d37c4ed719b54a64b84fa77ae4e90c5e_th.jpeg");
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello World!");
        testObject.put("dialogue", dialogue);
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.d("saved","success!");
                }
            }
        });
    }

    @Test
    public void constructBasicTopic() {
        AVObject topic = new AVObject("IStartedOpened");
        topic.put("name", "name");
        topic.put("brief", "brief");

        topic.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                } else {
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });
    }

    /*
    @Test
    public void readBasicTopic() {
        AVQuery<AVObject> avQuery = new AVQuery<>("PublishedOpened");
        avQuery.include("setting");
        avQuery.include("name");
        avQuery.include("brief");
        avQuery.include("dialogue");
        avQuery.orderByAscending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                List<Topic> topics = new ArrayList<>();
                if(avException == null) {
                    for(AVObject avObject: avObjects) {
                        Log.i("BrowseViewModel", "OK");
                        AVFile avFile = avObject.getAVFile("setting");
                        String name = (String) avObject.get("name");
                        String brief = (String) avObject.get("brief");
                        List<Map> dialogue_av = avObject.getList("dialogue");
                        List<String> members = avObject.getList("members");
                        List<com.thumbstage.hydrogen.model.vo.Line> dialouge = new ArrayList<>();
                        for(Map map: dialogue_av) {
                            Date date = AVUtils.dateFromString((String)map.get("when"));
                            dialouge.add(new com.thumbstage.hydrogen.model.vo.Line(
                                    (String) map.get("who"),
                                    date,
                                    (String) map.get("what"),
                                    (LineType.valueOf((String) map.get("type"))) ));
                        }
                        AVObject started_by = avObject.getAVObject("started_by");
                        String setting_url = avFile.getUrl();
                        Topic topic = Topic.Builder()
                                .setBrief(brief)
                                .setName(name)
                                .setMembers(members)
                                .setDialogue(dialouge)
                                .setSponsor(started_by.getObjectId())
                                .setSetting_id(setting_url);
                        topics.add(topic);
                    }
                } else {
                    avException.printStackTrace();
                }
            }
        });
        sleep(5);
    }

    @Test
    public void testConversation() {
        AVIMClient someone = AVIMClient.getInstance("5c63b5f344d90419c1acd242");
        someone.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if(e == null) {
                    client.openTopic(Arrays.asList("Jerry"), "testConversation", null, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVIMException e) {
                            if(e == null) {
                                AVIMTextMessage msg = new AVIMTextMessage();
                                msg.setFrom("5c500df444d904004dc13f71");
                                msg.setText("耗子，起床！");
                                conversation.sendMessage(msg, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        sleep(5);
    }

    @Test
    public void testSaveIStartedOpenedTopic() {
        Topic topic = new Topic();
        topic.setName("testSaveIStartedOpenedTopic");
        topic.setSetting_id("5c629287303f390047c13726");
        topic.setSponsor(AVUser.getCurrentUser().getObjectId());
        topic.getMembers().add(AVUser.getCurrentUser().getObjectId());
        CloudAPI.saveTopic2StartedOpened(topic);
        sleep(5);
    }


    @Test
    public void testCreateIStartedOpened() {
        AVObject topic = AVObject.createWithoutData(Topic.class.getSimpleName(), "5c6b6969a91c9300548c51a4");
        AVObject conv = AVObject.createWithoutData("_Conversation", "5c6a43478d6d81004e2798c9");
        AVObject record = new AVObject("IStartedOpened");
        record.put("topic",topic);
        record.put("conversation", conv);
        record.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Log.d("testCreateIStartedOpened","success!");
            }
        });
        sleep(5);
    }

    @Test
    public void testCreatePublishedOpened() {
        AVObject topic = AVObject.createWithoutData(Topic.class.getSimpleName(), "5c6b6969a91c9300548c51a4");
        AVObject conv = AVObject.createWithoutData("_Conversation", "5c6a43478d6d81004e2798c9");
        AVObject record = new AVObject("PublishedOpened");
        record.put("topic",topic);
        record.put("conversation", conv);
        record.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Log.d("testCreatePublishedOpened","success!");
            }
        });
        sleep(5);
    }

    @Test
    public void testCopyPublishedOpenedTopic() {
        Topic topic = new Topic();
        topic.setId("5c6ba28a303f39004749baa1");
        topic.setName("testCopyPublishedOpenedTopic");
        topic.setSetting_id("5c629287303f390047c13726");
        topic.setSponsor(AVUser.getCurrentUser().getObjectId());
        topic.getMembers().add(AVUser.getCurrentUser().getObjectId());
        CloudAPI.copyTopicFromPublishedOpened(topic, new CloudAPI.ICallBack() {
            @Override
            public void callback(String objectID) {
                Log.i("testCopyPublishedOpenedTopic", "success!");
            }
        });
        sleep(5);
    }

    @Test
    public void testCreateTopic() {
        final String userId = "5c500dbc303f394f8283eadc";
        Topic topic = new Topic();
        topic.setName("hello world");
        topic.setSponsor(userId);
        topic.setSetting_id("5c629287303f390047c13726");
        topic.setDialogue(null);
        topic.setDerive_from("5c6b6969a91c9300548c51a4");
        topic.setMembers(new ArrayList<String>(){{add(userId);}});
        topic.setBrief("for creating basic structure");
        AVObject object = AVObject.createWithoutData("_File",topic.getSetting_id());
        AVObject derive = AVObject.createWithoutData(Topic.class.getSimpleName(),topic.getDerive_from());
        AVObject record = new AVObject(Topic.class.getSimpleName());
        record.put("name",topic.getName());
        record.put("started_by", AVUser.getCurrentUser());
        record.put("dialogue", Arrays.asList(""));
        record.put("members", topic.getMembers());
        record.put("brief", topic.getBrief());
        record.put("setting", object);
        record.put("derive_from", derive);
        record.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Log.d("testCreateTopic","success!");
            }
        });
        sleep(5);
    }
    */

    @Test
    public void testDownloadFile() {
        final String fileID = "5c80795244d9040066d9e364";
        try {
            final AVFile file = AVFile.withObjectId(fileID);
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, AVException e) {
                    if(e == null) {
                        file.getUrl();
                        file.getName();
                        String suf = StringUtil.getSuffix(file.getUrl());
                        Context appContext = InstrumentationRegistry.getTargetContext();
                        File path = appContext.getFilesDir();
                        File outFile = new File(path, file.getName());
                        FileOutputStream stream = null;
                        try {
                            stream = new FileOutputStream(outFile);
                            stream.write(data);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            try {
                                stream.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Log.i("testDownloadFile", "ok");
                    }
                }
            });
        } catch (AVException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sleep(5);


    }

    @Test
    public void testUploadURLFile() {
        AVFile avFile = new AVFile("lj.jpeg", "http://img.mp.itc.cn/upload/20170315/d37c4ed719b54a64b84fa77ae4e90c5e_th.jpeg", new HashMap<String, Object>());
        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    Log.i("testUploadURLFile", "ok");
                } else {
                    e.printStackTrace();
                }
            }
        });
        sleep(5);
    }

    @Test
    public void testCloudAPI() {
        /*
        CloudAPI cloudAPI = new CloudAPI();
        cloudAPI.getMic(TopicTag.PUBLISHED, "", false, 0, new CloudAPI.IReturnMicList() {
            @Override
            public void callback(List<Mic> micList) {
                Log.i("testCloudAPI","");
            }
        });
        */
        sleep(10);
    }

    @Test
    public void testCloudAPIRaw() {
        AVQuery<AVObject> avQueryIs = new AVQuery<>("Topic");
        avQueryIs.whereEqualTo("is_finished", false);

        avQueryIs.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                Log.i("testCloudAPIRaw","");
                if (avException == null) {
                    for (AVObject avObject : avObjects) {
                        String type = (String) avObject.get("type");
                        boolean isFinished = (boolean) avObject.get("is_finished");
                        Log.i("testCloudAPIRaw", "");
                    }
                }
            }
        });



        sleep(10);
    }

    @Test
    public void testIMOpen() {
        AVIMClient client = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                Log.i("testIMOpen","testIMOpen open ok");
            }
        });
        sleep(10);
    }

    @Test
    public void testAddContact() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        CloudAPI cloudAPI = new CloudAPI(appContext);
        cloudAPI.addContact("5c500df444d904004dc13f71", "5c63b5f344d90419c1acd242", new IReturnBool() {
            @Override
            public void callback(Boolean isOK) {

            }
        });
        sleep(10);
    }

    @Test
    public void testGetMic() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        CloudAPI cloudAPI = new CloudAPI(appContext);
        cloudAPI.getMic(TopicTag.LITERAL,  "", false, 0, new IReturnMicList() {
            @Override
            public void callback(List<Mic> micList) {
                Log.i("testGetMic", "");
            }
        });
        sleep(10);
    }

}
