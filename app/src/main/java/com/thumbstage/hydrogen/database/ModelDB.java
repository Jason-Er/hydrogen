package com.thumbstage.hydrogen.database;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.text.TextUtils;

import com.thumbstage.hydrogen.database.entity.AtMeEntity;
import com.thumbstage.hydrogen.database.entity.ContactEntity;
import com.thumbstage.hydrogen.database.entity.LineEntity;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Setting;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.DataConvertUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelDB {

    private final HyDatabase database;
    private Executor executor;

    public final static int PER_PAGE_NUM = 15;
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    // TODO: 4/4/2019 remember to use db roomDB.runInTransaction
    @Inject
    public ModelDB(HyDatabase database, Executor executor) {
        this.database = database;
        this.executor = executor;
    }

    public boolean isTopicNeedFresh(TopicType type, final String started_by, final boolean isFinished) {
        if(TextUtils.isEmpty(started_by)) {
            return database.topicDao().hasTopic(type.name(), isFinished, getMaxRefreshTime(new Date())) == null;
        } else {
            return database.topicDao().hasTopic(type.name(), started_by, isFinished, getMaxRefreshTime(new Date())) == null;
        }
    }

    public boolean isContactNeedFresh(String userId) {
        return database.contractDao().hasContract(userId, getMaxRefreshTime(new Date())) == null;
    }

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    // region Model 2 entity
    public void saveTopic(final Topic topic) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                User user = topic.getStarted_by();
                saveUser(user);
                TopicEntity entity = new TopicEntity();
                entity.setId(topic.getId());
                entity.setName(topic.getName());
                entity.setBrief(topic.getBrief());
                entity.setDerive_from(topic.getDerive_from());
                entity.setStarted_by(topic.getStarted_by().getId());
                entity.setFinished(topic.isFinished());
                if( topic.getSetting()!=null ) {
                    entity.setSetting_url(topic.getSetting().getUrl());
                }
                entity.setLastRefresh(new Date());
                entity.setType(topic.getType().name());
                database.topicDao().insert(entity);
                saveMembers(DataConvertUtil.user2StringId(topic.getMembers()), topic.getId());
                saveLineList(topic.getDialogue(), topic.getId());
            }
        });
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

    public void saveAtMe(final AtMe atMe) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                saveMic(atMe.getMic());
                AtMeEntity entity = new AtMeEntity();
                entity.setMicId(atMe.getMic().getId());
                entity.setWhen(atMe.getWhen());
                entity.setWho(atMe.getWho().getId());
                entity.setWhat(atMe.getWhat());
                entity.setMe(atMe.getMe().getId());
                entity.setLastRefresh(new Date());
                database.atMeDao().insert(entity);
            }
        });
    }

    public void saveMic(final Mic mic) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                saveTopic(mic.getTopic());
                MicEntity entity = new MicEntity();
                entity.setId(mic.getId());
                entity.setTopicId(mic.getTopic().getId());
                entity.setLastRefresh(new Date());
                database.micDao().insert(entity);
            }
        });
    }

    public void saveMicList(final List<Mic> micList) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                for(Mic mic: micList) {
                    saveMic(mic);
                }
            }
        });
    }

    public void saveUser(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setAvatar(user.getAvatar());
        entity.setLastRefresh(new Date());
        database.userDao().insert(entity);
    }

    public void saveUserList(List<User> userList) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for(User user: userList) {
            if(user != null) {
                UserEntity entity = new UserEntity();
                entity.setName(user.getName());
                entity.setAvatar(user.getAvatar());
                entity.setId(user.getId());
                entity.setLastRefresh(new Date());
                userEntityList.add(entity);
            }
        }
        database.userDao().insert(userEntityList);
    }

    public void saveContacts(String userId, List<User> userList) {
        List<ContactEntity> contractEntities = new ArrayList<>();
        for(User user: userList) {
            if(user != null) {
                ContactEntity entity = new ContactEntity();
                entity.setUserId(userId);
                entity.setContractId(user.getId());
                entity.setLastRefresh(new Date());
                contractEntities.add(entity);
            }
        }
        database.contractDao().insert(contractEntities);
    }

    // endregion

    // region getter
    private MutableLiveData<List<AtMe>> atMeListLive = new MutableLiveData<>();
    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return Transformations.switchMap(database.atMeDao().get(meId, 15, pageNum * 15), new Function<List<AtMeEntity>, LiveData<List<AtMe>>>() {
            @Override
            public LiveData<List<AtMe>> apply(final List<AtMeEntity> input) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<AtMe> list = new ArrayList<>();
                        for(AtMeEntity entity:input) {
                            AtMe atMe = new AtMe();
                            Mic mic = getMic(entity.getMicId());
                            atMe.setMe(getUser(entity.getMe()));
                            atMe.setMic(mic);
                            atMe.setWhat(entity.getWhat());
                            atMe.setWhen(entity.getWhen());
                            atMe.setWho(getUser(entity.getWho()));
                            list.add(atMe);
                        }
                        atMeListLive.postValue(list);
                    }
                });
                return atMeListLive;
            }
        });
    }

    private Map<String, MutableLiveData<List<Mic>>> liveDataMap = new HashMap<>();
    public LiveData<List<Mic>> getMic(TopicType type, String started_by, boolean isFinished, int pageNum) {

        final MutableLiveData<List<Mic>> micListLive;
        String key = type.name()+isFinished;
        if(!TextUtils.isEmpty(started_by)) {
            key += "personal";
        }
        if(liveDataMap.containsKey(key)) {
            micListLive = liveDataMap.get(key);
        } else {
            micListLive = new MutableLiveData<>();
            liveDataMap.put(key, micListLive);
        }

        LiveData<List<MicEntity>> liveData;
        if(TextUtils.isEmpty(started_by)) {
            liveData = database.micDao().get(type.name(), isFinished, PER_PAGE_NUM, pageNum * PER_PAGE_NUM);
        } else {
            liveData = database.micDao().get(type.name(), started_by, isFinished, PER_PAGE_NUM, pageNum*PER_PAGE_NUM);
        }
        return Transformations.switchMap(liveData, new Function<List<MicEntity>, LiveData<List<Mic>>>() {
            @Override
            public LiveData<List<Mic>> apply(final List<MicEntity> input) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Mic> micList = new ArrayList<>();
                        for(MicEntity entity: input) {
                            Mic mic = getMic(entity.getId());
                            micList.add(mic);
                        }
                        micListLive.postValue(micList);
                    }
                });
                return micListLive;
            }
        });
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
        List<UserEntity> userEntities = database.topicUserDao().get(topicId);
        return entity2User(userEntities);
    }

    private List<User> entity2User(List<UserEntity> userEntityList) {
        List<User> users = new ArrayList<>();
        for(UserEntity entity: userEntityList) {
            users.add(new User(entity.getId(), entity.getName(), entity.getAvatar()));
        }
        return users;
    }

    public LiveData<List<User>> getContact(String userId, int pageNum) {
        return Transformations.map(database.contractDao().get(userId, PER_PAGE_NUM, pageNum * PER_PAGE_NUM), new Function<List<UserEntity>, List<User>>() {
            @Override
            public List<User> apply(List<UserEntity> input) {
                return entity2User(input);
            }
        });
    }

    public User getUser(String userId) {
        UserEntity entity = database.userDao().get(userId);
        User user = new User(entity.getId(), entity.getName(), entity.getAvatar());
        return user;
    }

    public Mic getMic(String id) {
        MicEntity entity = database.micDao().get(id);
        Mic mic = new Mic();
        Topic topic = getTopic(entity.getTopicId());
        mic.setTopic(topic);
        mic.setId(entity.getId());
        return mic;
    }

    public List<Mic> getMic(List<String> ids) {
        List<Mic> mics = new ArrayList<>();
        for(String id: ids) {
            Mic mic = getMic(id);
            mics.add(mic);
        }
        return mics;
    }

    public Topic getTopic(String id) {
        TopicEntity entity = database.topicDao().get(id);
        Topic topic = new Topic();
        topic.setId(entity.getId());
        topic.setType(TopicType.valueOf(entity.getType()));
        topic.setName(entity.getName());
        topic.setBrief(entity.getName());
        topic.setSetting(new Setting("", entity.getSetting_url(), true));
        topic.setDerive_from(entity.getDerive_from());
        topic.setDialogue(getLine(entity.getId()));
        topic.setStarted_by(getUser(entity.getStarted_by()));
        topic.setMembers(getMembers(entity.getId()));
        return topic;
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

    public void deleteAtMe(AtMe atMe) {
        database.atMeDao().deleteAtMeByKey(atMe.getMic().getId(), atMe.getMe().getId());
    }
}
