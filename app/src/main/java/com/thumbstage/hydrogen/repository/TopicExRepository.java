package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.LineEntity;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicExEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Setting;
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

    public void getTopicEx(TopicExType type, String started_by, int pageNum) {
        switch (type) {
            case PUBLISHED_OPENED:
                getTopicEx(type, "", pageNum, publishedOpened);
                break;
            case IPUBLISHED_OPENED:
                break;
            case ISTARTED_OPENED:
                break;
            case IATTENDED_OPENED:
                break;
        }
    }

    private void getTopicEx(final TopicExType type, final String started_by, final int pageNum, final MutableLiveData<List<TopicEx>> mutableLiveData) {
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
                    // TODO: 3/15/2019 perPageNum need refactoring
                    List<TopicEx> topicExes = getTopicExByPage(type, started_by, pageNum, 15);
                    mutableLiveData.postValue(topicExes);
                }
            }
        });
    }

    private List<TopicEx> getTopicExByPage(TopicExType type, String started_by, int pageNum, int perPageNum) {

        List<TopicEx> topicExes = new ArrayList<>();
        List<TopicExEntity> topicExEntityList = null;
        if(started_by != null) {
            topicExEntityList = database.topicExDao().get(type.name, started_by, perPageNum, pageNum*perPageNum);
        } else {
            topicExEntityList = database.topicExDao().get(type.name,  perPageNum, pageNum*perPageNum);
        }
        List<String> topicIds = new ArrayList<>();
        List<String> micIds = new ArrayList<>();
        for(TopicExEntity entity: topicExEntityList) {
            topicIds.add(entity.getTopicId());
            micIds.add(entity.getMicId());
        }
        List<Topic> topics = getTopic(topicIds);
        List<Mic> mics = getMic(micIds);
        for(TopicExEntity entity: topicExEntityList) {
            int i = topicExEntityList.indexOf(entity);
            TopicEx topicEx = new TopicEx(topics.get(i), mics.get(i));
            topicExes.add(topicEx);
        }

        return topicExes;
    }

    private List<Line> getLine(String topicId) {
        List<Line> lines = new ArrayList<>();
        List<LineEntity> lineEntities = database.lineDao().get(topicId);
        for(LineEntity entity: lineEntities) {
            Line line = new Line(entity.getWho(), entity.getWhen(), entity.getWhat(), LineType.valueOf(entity.getLine_type()));
            lines.add(line);
        }
        return lines;
    }

    private List<String> getMembers(String topicId) {
        List<String> users = new ArrayList<>();
        List<TopicUserEntity> topicUserEntities = database.topicUserDao().get(topicId);
        for(TopicUserEntity entity: topicUserEntities) {
            users.add(entity.getUserId());
        }
        return users;
    }

    private User getUser(String userId) {
        UserEntity entity = database.userDao().get(userId);
        User user = new User(entity.getId(), entity.getName(), entity.getAvatar());
        return user;
    }

    private List<Mic> getMic(List<String> ids) {
        List<Mic> mics = new ArrayList<>();
        List<MicEntity> micEntities = database.micDao().get(ids);
        for(MicEntity entity: micEntities) {
            Mic mic = new Mic(entity.getId());
            mics.add(mic);
        }
        return mics;
    }

    private List<Topic> getTopic(List<String> ids) {
        List<Topic> topics = new ArrayList<>();
        List<TopicEntity> topicEntityList = database.topicDao().get(ids);
        for(TopicEntity entity: topicEntityList) {
            Topic topic = new Topic();
            topic.setId(entity.getId());
            topic.setName(entity.getName());
            topic.setBrief(entity.getName());
            topic.setSetting(new Setting("", entity.getSetting_url(), true));
            topic.setDerive_from(entity.getDerive_from());
            topic.setDialogue(getLine(entity.getId()));
            topic.setStarted_by(getUser(entity.getStarted_by()));
            topic.setMembers(getMembers(entity.getId()));
        }
        return topics;
    }

    private void saveLineList(List<Line> lineList, String topicId) {
        List<LineEntity> lineEntities = new ArrayList<>();
        for(Line line: lineList) {
            LineEntity entity = new LineEntity();
            entity.setWho(line.getWho());
            entity.setWhen(line.getWhen());
            entity.setWhat(line.getWhat());
            entity.setInWhichTopic(topicId);
            entity.setLine_type(line.getLineType().name());
            lineEntities.add(entity);
        }
        database.lineDao().insert(lineEntities);
    }

    private void saveMembers(List<String> members, String topicId) {
        List<String> userIds = new ArrayList<>();
        userIds.addAll(members);

        List<UserEntity> userEntityList = database.userDao().get(members);
        for(UserEntity entity: userEntityList) {
            if(members.contains(entity.getId())) {
                userIds.remove(entity.getId());
            }
        }
        userEntityList.clear();
        saveUserIds(userIds);
        List<TopicUserEntity> topicUserEntityList = new ArrayList<>();
        for(String userId: members) {
            TopicUserEntity entity = new TopicUserEntity();
            entity.setTopicId(topicId);
            entity.setUserId(userId);
            topicUserEntityList.add(entity);
        }
        database.topicUserDao().insert(topicUserEntityList);

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

                saveLineList(topic.getDialogue(), topic.getId());
                saveMembers(topic.getMembers(), topic.getId());
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

    private void saveUserIds(List<String> userIds) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for(String userId: userIds) {
            UserEntity entity = new UserEntity();
            entity.setId(userId);
            entity.setLastRefresh(new Date());
            userEntityList.add(entity);

        }
        database.userDao().insert(userEntityList);
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
