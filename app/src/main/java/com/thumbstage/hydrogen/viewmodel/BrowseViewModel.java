package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.TopicEx;

import java.util.List;

public class BrowseViewModel extends ViewModel {

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


}
