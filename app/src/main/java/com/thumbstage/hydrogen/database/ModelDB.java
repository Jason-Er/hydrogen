package com.thumbstage.hydrogen.database;

import android.util.Log;

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
import com.thumbstage.hydrogen.utils.DataConvertUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelDB {

    private final HyDatabase database;

    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    @Inject
    public ModelDB(HyDatabase database) {
        this.database = database;
    }

    public boolean isTopicExNeedFresh(TopicExType type) {
        return database.topicExDao().hasTopicEx(type.name, getMaxRefreshTime(new Date())) == null;
    }

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    // region Model 2 entity
    public void saveTopicList(List<Topic> topicList) {
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
                long id = database.topicDao().insert(entity);
                saveLineList(topic.getDialogue(), topic.getId());
                saveMembers(DataConvertUtil.user2StringId(topic.getMembers()), topic.getId());
            }
        }
        // database.topicDao().insert(topicEntities);
    }

    public void saveMembers(List<String> members, String topicId) {
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

    public void saveUserIds(List<String> userIds) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for(String userId: userIds) {
            UserEntity entity = new UserEntity();
            entity.setId(userId);
            entity.setLastRefresh(new Date());
            userEntityList.add(entity);

        }
        database.userDao().insert(userEntityList);
    }

    public void saveLineList(List<Line> lineList, String topicId) {
        List<LineEntity> lineEntities = new ArrayList<>();
        for(Line line: lineList) {
            LineEntity entity = new LineEntity();
            entity.setWho(line.getWho().getId());
            entity.setWhen(line.getWhen());
            entity.setWhat(line.getWhat());
            entity.setInWhichTopic(topicId);
            entity.setLine_type(line.getLineType().name());
            lineEntities.add(entity);
        }
        database.lineDao().insert(lineEntities);
    }

    public void saveTopicExList(List<TopicEx> topicExList, TopicExType type) {
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
        List<Long> ids = database.topicExDao().insert(topicExEntities);
        // for debug
        List<TopicExEntity> entityList = database.topicExDao().get(type.name, 15, 0);
        Log.i("saveTopicExList", "ok");
    }

    public void saveMicList(List<Mic> micList) {
        List<MicEntity> micEntities = new ArrayList<>();
        for(Mic mic: micList) {
            if(mic != null && mic.getId() != null) {
                MicEntity entity = new MicEntity();
                entity.setId(mic.getId());
                micEntities.add(entity);
            }
        }
        List<Long> ids = database.micDao().insert(micEntities);
        // for debug
        MicEntity entity = database.micDao().get(micEntities.get(0).getId());
        Log.i("saveMicList", "ok");
    }

    public void saveUserList(List<User> userList) {
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

    // endregion

    // region getter
    public List<TopicEx> getTopicExByPage(TopicExType type, String started_by, int pageNum, int perPageNum) {

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

    public List<Line> getLine(String topicId) {
        List<Line> lines = new ArrayList<>();
        List<LineEntity> lineEntities = database.lineDao().get(topicId);
        for(LineEntity entity: lineEntities) {
            Line line = new Line(getUser(entity.getWho()), entity.getWhen(), entity.getWhat(), LineType.valueOf(entity.getLine_type()));
            lines.add(line);
        }
        return lines;
    }

    public List<User> getMembers(String topicId) {
        List<User> users = new ArrayList<>();
        List<UserEntity> userEntities = database.topicUserDao().get(topicId);
        for(UserEntity entity: userEntities) {
            users.add(new User(entity.getId(), entity.getName(), entity.getAvatar()));
        }
        return users;
    }

    public User getUser(String userId) {
        UserEntity entity = database.userDao().get(userId);
        User user = new User(entity.getId(), entity.getName(), entity.getAvatar());
        return user;
    }

    public List<Mic> getMic(List<String> ids) {
        List<Mic> mics = new ArrayList<>();
        List<MicEntity> micEntities = database.micDao().get(ids);
        for(MicEntity entity: micEntities) {
            Mic mic = new Mic(entity.getId());
            mics.add(mic);
        }
        return mics;
    }

    public List<Topic> getTopic(List<String> ids) {
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
    // endregion
}
