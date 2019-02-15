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
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

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
                        List<com.thumbstage.hydrogen.model.Line> dialouge = new ArrayList<>();
                        for(Map map: dialogue_av) {
                            Date date = AVUtils.dateFromString((String)map.get("when"));
                            dialouge.add(new com.thumbstage.hydrogen.model.Line(
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
                                .setStarted_by(started_by.getObjectId())
                                .setSetting_url(setting_url);
                        topics.add(topic);
                    }
                } else {
                    avException.printStackTrace();
                }
            }
        });
        sleep(5);
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
