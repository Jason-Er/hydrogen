package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.TopicExType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TopicExRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    private MutableLiveData<List<TopicEx>> publishedOpened = new MutableLiveData<>();
    private MutableLiveData<List<TopicEx>> iStartedOpened = new MutableLiveData<>();
    private MutableLiveData<List<TopicEx>> iAttendedOpened = new MutableLiveData<>();

    @Inject
    public TopicExRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    public LiveData<List<TopicEx>> getPublishedOpened() {
        return publishedOpened;
    }

    public LiveData<List<TopicEx>> getIStartedOpened() {
        return iStartedOpened;
    }

    public LiveData<List<TopicEx>> getIAttendedOpened() {
        return iAttendedOpened;
    }

    public void getTopicEx(TopicExType type, String started_by, int pageNum) {
        switch (type) {
            case PUBLISHED_OPENED:
                getTopicEx(type, started_by, pageNum, publishedOpened);
                break;
            case IPUBLISHED_OPENED:

                break;
            case ISTARTED_OPENED:
                getTopicEx(type, started_by, pageNum, iStartedOpened);
                break;
            case IATTENDED_OPENED:
                getTopicEx(type, started_by, pageNum, iAttendedOpened);
                break;
        }
    }

    private void getTopicEx(final TopicExType type, final String started_by, final int pageNum, final MutableLiveData<List<TopicEx>> mutableLiveData) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
            if( modelDB.isTopicExNeedFresh(type) ) {
                cloudAPI.getTopicEx(type, pageNum, new CloudAPI.ITopicExCallBack() {
                    @Override
                    public void callback(final List<TopicEx> topicExList) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<Topic> topics = new ArrayList<>();
                            List<Mic> mics = new ArrayList<>();
                            for(TopicEx topicEx:topicExList) {
                                topics.add(topicEx.getTopic());
                                mics.add(topicEx.getMic());
                            }
                            modelDB.saveTopicList(topics);
                            modelDB.saveMicList(mics);
                            modelDB.saveTopicExList(topicExList, type);
                            mutableLiveData.postValue(topicExList);
                        }
                    });
                    }
                });
            } else {
                // TODO: 3/15/2019 perPageNum need refactoring
                List<TopicEx> topicExes = modelDB.getTopicExByPage(type, started_by, pageNum, 15);
                mutableLiveData.postValue(topicExes);
            }
            }
        });
    }

}
