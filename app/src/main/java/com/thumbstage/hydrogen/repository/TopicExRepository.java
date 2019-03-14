package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicExEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.model.TopicExType;
import com.thumbstage.hydrogen.model.User;

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

    public void getTopicEx(TopicExType type, int pageNum) {
        switch (type) {
            case PUBLISHED_OPENED:
                getTopicEx(type, pageNum, publishedOpened);
                break;
            case IPUBLISHED_OPENED:
                break;
            case ISTARTED_OPENED:
                break;
            case IATTENDED_OPENED:
                break;
        }
    }

    private void getTopicEx(final TopicExType type, final int pageNum, final MutableLiveData<List<TopicEx>> mutableLiveData) {
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
                                    saveTopicExList(topicExList, type);
                                    mutableLiveData.postValue(topicExList);
                                }
                            });
                        }
                    });
                } else {
                    // TODO: 3/14/2019 get from repository and merge to model


                }
            }
        });
    }

    private void saveTopicList(List<Topic> topicList) {
        List<User> users = new ArrayList<>();
        for(Topic topic: topicList) {
            users.add(topic.getStarted_by());
        }
        saveUserList(users);
        List<TopicEntity> topicEntities = new ArrayList<>();
        for(Topic topic: topicList) {
            if(topic != null) {
                TopicEntity entity = new TopicEntity();
                entity.setId(topic.getId());
                entity.setName(topic.getName());
                entity.setBrief(topic.getBrief());
                entity.setDerive_from(topic.getDerive_from());
                entity.setStarted_by(topic.getStarted_by().getId());
                entity.setSetting_url(topic.getSetting().getUrl());
                entity.setLastRefresh(new Date());
                topicEntities.add(entity);
            }
        }
        database.topicDao().insert(topicEntities);
    }

    private void saveMicList(List<Mic> micList) {
        List<MicEntity> micEntities = new ArrayList<>();
        for(Mic mic: micList) {
            if(mic != null && mic.getId() != null) {
                MicEntity entity = new MicEntity();
                entity.setId(mic.getId());
                micEntities.add(entity);
            }
        }
        database.micDao().insert(micEntities);
    }

    private void saveTopicExList(List<TopicEx> topicExList, TopicExType type) {
        List<TopicExEntity> topicExEntities = new ArrayList<>();
        for(TopicEx topicEx: topicExList) {
            if(topicEx != null) {
                TopicExEntity entity = new TopicExEntity();
                entity.setTopicId(topicEx.getTopic().getId());
                entity.setMicId(topicEx.getMic()==null? null: topicEx.getMic().getId());
                entity.setLastRefresh(new Date());
                entity.setType(type.name());
                topicExEntities.add(entity);
            }
        }
        database.topicExDao().insert(topicExEntities);
    }

    private void saveUserList(List<User> userList) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for(User user: userList) {
            if(user != null) {
                UserEntity entity = new UserEntity();
                entity.setId(user.getId());
                entity.setName(user.getName());
                entity.setAvatar(user.getAvatar());
                entity.setLastRefresh(new Date());
                userEntityList.add(entity);
            }
        }
        database.userDao().insert(userEntityList);
    }

    // ---

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
