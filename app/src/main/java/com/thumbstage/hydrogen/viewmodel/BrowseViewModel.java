package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

public class BrowseViewModel extends ViewModel {

    private final MutableLiveData<List<AVIMConversation>> publishedOpened = new MutableLiveData<>();
    private final MutableLiveData<List<AVIMConversation>> iStartedOpened = new MutableLiveData<>();

    public MutableLiveData<List<AVIMConversation>> getPublishedOpened() {
        return publishedOpened;
    }
    public MutableLiveData<List<AVIMConversation>> getIStartedOpened() {
        return iStartedOpened;
    }

    public void getPublishedOpenedByPageNum(int pageNum) {
        AVQuery<AVObject> avQuery = new AVQuery<>("PublishedOpened");
        avQuery.orderByAscending("createdAt");
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
                                publishedOpened.setValue(conversationList);
                            }
                        });
                    } else {
                        List<AVIMConversation> conversationList = new ArrayList<>();
                        for (String convId : convIdList) {
                            conversationList.add( LCChatKit.getInstance().getClient().getConversation(convId) );
                        }
                        publishedOpened.setValue(conversationList);
                    }
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
