package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.TopicExEntity;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.TopicExType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TopicExRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final HyDatabase database;
    private final Executor executor;

    private MutableLiveData<List<TopicEx>> publishedOpened = new MutableLiveData<>();;

    @Inject
    public TopicExRepository(HyDatabase database, Executor executor) {
        this.database = database;
        this.executor = executor;
    }

    public LiveData<List<TopicEx>> getPublishedOpened() {
        return publishedOpened;
    }

    public LiveData<TopicEx> getTopicEx(TopicExType type, int pageNum) {
        refreshTopicEx(type, pageNum); // try to refresh data if possible from Github Api
        List<TopicExEntity> entityList= database.topicExDao().get(type.name);
        return null;
    }

    private void refreshTopicEx(final TopicExType type, final int pageNum) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                boolean topicExExists = (database.topicExDao().hasTopicEx(type.name, getMaxRefreshTime(new Date())) != null);
                if(!topicExExists) {
                    LCRepository.getTopicEx(type, pageNum, new LCRepository.ITopicExCallBack() {
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
                                    saveTopicList(topics);
                                    saveMicList(mics);
                                    saveTopicExList(topicExList);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void saveTopicList(List<Topic> topicList) {

    }

    private void saveMicList(List<Mic> micList) {

    }

    private void saveTopicExList(List<TopicEx> topicExList) {

    }

    // ---

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
