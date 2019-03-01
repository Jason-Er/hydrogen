package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.im.IIMCallBack;
import com.thumbstage.hydrogen.im.IMConversationHandler;
import com.thumbstage.hydrogen.im.IMMessageHandler;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.model.TopicEx;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;

public class BrowseViewModel extends ViewModel {

    final String TAG = "BrowseViewModel";
    private final MutableLiveData<List<Pipe>> atMe = new MutableLiveData<>();
    private final MutableLiveData<List<TopicEx>> publishedOpened = new MutableLiveData<>();
    private final MutableLiveData<List<TopicEx>> iStartedOpened = new MutableLiveData<>();
    private final MutableLiveData<List<TopicEx>> iAttendedOpened = new MutableLiveData<>();

    public MutableLiveData<List<TopicEx>> getPublishedOpened() {
        return publishedOpened;
    }
    public MutableLiveData<List<TopicEx>> getIStartedOpened() {
        return iStartedOpened;
    }
    public MutableLiveData<List<TopicEx>> getIAttendedOpened() {
        return iAttendedOpened;
    }
    public MutableLiveData<List<Pipe>> getAtMe() {
        return atMe;
    }

    public void getIAttendedOpenedByPageNum(int pageNum) {
        LCRepository.getTopicEx(LCRepository.TopicExType.IATTENDED_OPENED, pageNum, new LCRepository.ITopicExCallBack() {
            @Override
            public void callback(List<TopicEx> topicExList) {
                iAttendedOpened.setValue(topicExList);
            }
        });
    }

    public void getPublishedOpenedByPageNum(int pageNum) {
        LCRepository.getTopicEx(LCRepository.TopicExType.PUBLISHED_OPENED, pageNum, new LCRepository.ITopicExCallBack() {
            @Override
            public void callback(List<TopicEx> topicExList) {
                publishedOpened.setValue(topicExList);
            }
        });
    }

    public void getIStartedOpenedByPageNum(int pageNum) {
        LCRepository.getTopicEx(LCRepository.TopicExType.ISTARTED_OPENED, pageNum, new LCRepository.ITopicExCallBack() {
            @Override
            public void callback(List<TopicEx> topicExList) {
                iStartedOpened.setValue(topicExList);
            }
        });
    }

    public void getAtMeByPageNum(int pageNum) {
        updateConversationList();
    }

    public void setListenIMCallBack(IMMessageHandler imMessageHandler) {
        imMessageHandler.setCallback(new IIMCallBack() {
            @Override
            public void callBack() {
                updateConversationList();
            }
        });
    }

    public void setListenIMCallBack(IMConversationHandler imConversationHandler) {
        imConversationHandler.setCallback(new IIMCallBack() {
            @Override
            public void callBack() {
                updateConversationList();
            }
        });
    }

    private void updateConversationList() {
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        List<Pipe> pipeList = new ArrayList<>();
        // List<AVIMConversation> conversationList = new ArrayList<>();
        for (String convId : convIdList) {
            pipeList.add(new Pipe(convId));
            // conversationList.add(UserGlobal.getInstance().getClient().getConversation(convId));
        }
        atMe.setValue(pipeList);
    }

}
