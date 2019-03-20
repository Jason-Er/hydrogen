package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.TopicExType;
import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.im.IIMCallBack;
import com.thumbstage.hydrogen.im.IMConversationHandler;
import com.thumbstage.hydrogen.im.IMMessageHandler;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.repository.AtMeRepository;
import com.thumbstage.hydrogen.repository.TopicExRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;

@Singleton
public class BrowseViewModel extends ViewModel {

    private TopicExRepository topicExRepository;
    private AtMeRepository atMeRepository;

    final String TAG = "BrowseViewModel";

    public LiveData<List<TopicEx>> getPublishedOpened() {
        return topicExRepository.getPublishedOpened();
    }
    public LiveData<List<TopicEx>> getIStartedOpened() {
        return topicExRepository.getIStartedOpened();
    }
    public LiveData<List<TopicEx>> getIAttendedOpened() {
        return topicExRepository.getIAttendedOpened();
    }

    @Inject
    public BrowseViewModel(TopicExRepository topicExRepository, AtMeRepository atMeRepository) {
        this.topicExRepository = topicExRepository;
        this.atMeRepository = atMeRepository;
    }

    public void getIAttendedOpenedByPageNum(int pageNum) {
        topicExRepository.getTopicEx(TopicExType.IATTENDED_OPENED, UserGlobal.getInstance().getCurrentUserId(), pageNum);
    }

    public void getPublishedOpenedByPageNum(int pageNum) {
        topicExRepository.getTopicEx(TopicExType.PUBLISHED_OPENED, "", pageNum);
    }

    public void getIStartedOpenedByPageNum(int pageNum) {
        topicExRepository.getTopicEx(TopicExType.ISTARTED_OPENED, UserGlobal.getInstance().getCurrentUserId(), pageNum);
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return atMeRepository.getAtMeByPageNum(meId, pageNum);
    }

}
