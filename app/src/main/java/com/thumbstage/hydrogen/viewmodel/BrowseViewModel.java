package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKit;

public class BrowseViewModel extends ViewModel {

    private final MutableLiveData<List<Topic>> publishedOpened = new MutableLiveData<>();
    private final MutableLiveData<List<AVIMConversation>> iStartedOpened = new MutableLiveData<>();

    public MutableLiveData<List<Topic>> getPublishedOpened() {
        return publishedOpened;
    }
    public MutableLiveData<List<AVIMConversation>> getIStartedOpened() {
        return iStartedOpened;
    }

    public void getPublishedOpenedByPageNum(int pageNum) {
        AVQuery<AVObject> avQuery = new AVQuery<>("PublishedOpened");
        avQuery.include("setting");
        avQuery.include("name");
        avQuery.include("brief");
        avQuery.include("dialogue");
        avQuery.include("members");
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
                        List<Map> datalist = avObject.getList("dialogue");
                        List<String> members = avObject.getList("members");
                        List<Line> dialogue = new ArrayList<>();
                        for(Map map: datalist) {
                            dialogue.add(new Line(
                                    (String) map.get("who"),
                                    StringUtil.string2Date((String) map.get("when")),
                                    (String) map.get("what"),
                                    (LineType.valueOf((String) map.get("type"))) ));
                        }
                        AVObject started_by = avObject.getAVObject("started_by");
                        String setting_url = avFile.getUrl();
                        Topic topic = Topic.Builder()
                                .setBrief(brief)
                                .setName(name)
                                .setDialogue(dialogue)
                                .setMembers(members)
                                .setStarted_by(started_by.getObjectId())
                                .setSetting_id(setting_url);
                        topics.add(topic);
                    }
                    publishedOpened.setValue(topics);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

    public void getIStartedOpenedByPageNum(int pageNum) {
        AVQuery<AVObject> avQuery = new AVQuery<>("IStartedOpened");
        avQuery.orderByAscending("createdAt");
        avQuery.whereEqualTo("started_by", AVUser.getCurrentUser().getObjectId());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                final List<String> convIdList = new ArrayList<>();
                if(avException == null) {
                    for(AVObject avObject: avObjects) {
                        AVObject conversation = avObject.getAVObject("conversation");
                        Log.i("BrowseViewModel", conversation.toString());
                        String conversationId = conversation.getObjectId();
                        convIdList.add(conversationId);
                    }
                    if( LCChatKit.getInstance().getClient() == null ) {
                        LCChatKit.getInstance().open(AVUser.getCurrentUser().getObjectId(), new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient client, AVIMException e) {
                                List<AVIMConversation> conversationList = new ArrayList<>();
                                for (String convId : convIdList) {
                                    conversationList.add( LCChatKit.getInstance().getClient().getConversation(convId) );
                                }
                                iStartedOpened.setValue(conversationList);
                            }
                        });
                    } else {
                        List<AVIMConversation> conversationList = new ArrayList<>();
                        for (String convId : convIdList) {
                            conversationList.add( LCChatKit.getInstance().getClient().getConversation(convId) );
                        }
                        iStartedOpened.setValue(conversationList);
                    }
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }


}
