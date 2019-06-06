package com.thumbstage.hydrogen.database;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.DataSource;
import android.text.TextUtils;

import com.thumbstage.hydrogen.app.Config;
import com.thumbstage.hydrogen.database.entity.ContactEntity;
import com.thumbstage.hydrogen.database.entity.LineEntity;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicTagEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserCanEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.bo.MessageType;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.model.vo.Topic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.DataConvertUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public boolean isTopicNeedFresh(TopicTag tag, final String sponsor, final boolean isFinished) {
        if(TextUtils.isEmpty(sponsor)) {
            return database.topicDao().hasTopic(tag.name(), isFinished, getMaxRefreshTime(new Date())) == null;
        } else {
            return database.topicDao().hasTopic(tag.name(), sponsor, isFinished, getMaxRefreshTime(new Date())) == null;
        }
    }

    public boolean isContactNeedFresh(String userId) {
        return database.contractDao().hasContract(userId, getMaxRefreshTime(new Date())) == null;
    }

    public boolean isUserNeedFresh(String userId) {
        return database.userDao().hasUser(userId, getMaxRefreshTime(new Date())) == null;
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
                User user = topic.getSponsor();
                saveUser(user);
                TopicEntity entity = new TopicEntity();
                entity.setId(topic.getId());
                entity.setName(topic.getName());
                entity.setBrief(topic.getBrief());
                entity.setDerive_from(topic.getDerive_from());
                entity.setSponsor(topic.getSponsor().getId());
                entity.setFinished(topic.isFinished());
                if( topic.getSetting()!=null ) {
                    entity.setSetting_url(topic.getSetting().getUrl());
                }
                entity.setUpdateAt(topic.getUpdateAt());
                entity.setLastRefresh(new Date());
                database.topicDao().insert(entity);
                saveTag(topic.getId(), topic.getTags());
                saveUserCan(topic.getId(),topic.getUserCan());
                saveMembers(DataConvertUtil.user2StringId(topic.getMembers()), topic.getId());
                saveLineList(topic.getDialogue(), topic.getId());
            }
        });
    }

    private void saveUserCan(String topicId, Map<String, Set<CanOnTopic>> userCanMap) {
        List<TopicUserCanEntity> entities = new ArrayList<>();
        if(userCanMap != null) {
            for (String userId : userCanMap.keySet()) {
                for (CanOnTopic can : userCanMap.get(userId)) {
                    TopicUserCanEntity entity = new TopicUserCanEntity();
                    entity.setTopicId(topicId);
                    entity.setUserId(userId);
                    entity.setCan(can.name());
                    entities.add(entity);
                }
            }
        }
        database.topicUserCanDao().insert(entities);
    }

    private void saveTag(String topicId, Set<TopicTag> tags) {
        List<TopicTagEntity> entities = new ArrayList<>();
        for(TopicTag tag: tags) {
            TopicTagEntity entity = new TopicTagEntity();
            entity.setTopicId(topicId);
            entity.setTag(tag.name());
            entities.add(entity);
        }
        database.topicTagDao().insert(entities);
    }

    public void saveMembers(List<String> members, String topicId) {
        if(members!=null && members.size()>0) {
            List<String> userIds = new ArrayList<>();
            userIds.addAll(members);

            List<UserEntity> userEntityList = database.userDao().get(members);
            for (UserEntity entity : userEntityList) {
                if (members.contains(entity.getId())) {
                    userIds.remove(entity.getId());
                }
            }
            userEntityList.clear();
            saveUserIds(userIds);
            List<TopicUserEntity> topicUserEntityList = new ArrayList<>();
            for (String userId : members) {
                TopicUserEntity entity = new TopicUserEntity();
                entity.setTopicId(topicId);
                entity.setUserId(userId);
                topicUserEntityList.add(entity);
            }
            database.topicUserDao().insert(topicUserEntityList);
        }

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
            entity.setMessage_type(line.getMessageType().name());
            lineEntities.add(entity);
        }
        database.lineDao().insert(lineEntities);
    }

    public void saveMic(final Mic mic) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                saveTopic(mic.getTopic());
                String id = database.micDao().getItemId(mic.getId());
                if(id == null) {
                    MicEntity entity = new MicEntity();
                    entity.setId(mic.getId());
                    entity.setTopicId(mic.getTopic().getId());
                    entity.setUpdateAt(mic.getUpdateAt());
                    entity.setLastRefresh(new Date());
                    database.micDao().insert(entity);
                } else {
                    database.micDao().update(mic.getId(), mic.getTopic().getId(), new Date());
                }
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

    public void saveContacts(final String userId, final List<User> userList) {
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
    public DataSource.Factory<Integer, Mic> getMic(TopicTag tag, boolean isFinished) {
        return database.micDao()
                .get(tag.name(),isFinished)
                .map(new Function<MicEntity, Mic>() {
                    @Override
                    public Mic apply(MicEntity input) {
                        return convert2Mic(input);
                    }
                });
    }

    public DataSource.Factory<Integer, Mic> getMic(TopicTag tag, String userId, boolean isFinished) {
        return database.micDao()
                .get(tag.name(),userId,isFinished)
                .map(new Function<MicEntity, Mic>() {
                    @Override
                    public Mic apply(MicEntity input) {
                        return convert2Mic(input);
                    }
                });
    }

    private Map<String, MutableLiveData<List<Mic>>> liveDataMap = new HashMap<>();
    public LiveData<List<Mic>> getMic(TopicTag tag, String userId, boolean isFinished, int pageNum) {

        final MutableLiveData<List<Mic>> micListLive;
        String key = tag.name()+isFinished;
        if(!TextUtils.isEmpty(userId)) {
            key += "personal";
        }
        if(liveDataMap.containsKey(key)) {
            micListLive = liveDataMap.get(key);
        } else {
            micListLive = new MutableLiveData<>();
            liveDataMap.put(key, micListLive);
        }

        LiveData<List<MicEntity>> liveData;
        if(TextUtils.isEmpty(userId)) {
            liveData = database.micDao().get(tag.name(), isFinished, PER_PAGE_NUM, pageNum * PER_PAGE_NUM);
        } else {
            liveData = database.micDao().get(tag.name(), userId, isFinished, PER_PAGE_NUM, pageNum*PER_PAGE_NUM);
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
            line.setMessageType(MessageType.valueOf(entity.getMessage_type()));
            lines.add(line);
        }
        return lines;
    }

    public List<User> getMembers(String topicId) {
        List<UserEntity> userEntities = database.topicUserDao().get(topicId);
        return entity2User(userEntities);
    }

    private User entity2User(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getAvatar());
    }

    private List<User> entity2User(List<UserEntity> userEntityList) {
        List<User> users = new ArrayList<>();
        for(UserEntity entity: userEntityList) {
            users.add(new User(entity.getId(), entity.getName(), entity.getAvatar()));
        }
        return users;
    }

    public LiveData<List<User>> getUsers(List<String> userIds) {
        return Transformations.map(database.userDao().getLive(userIds), new Function<List<UserEntity>, List<User>>() {
            @Override
            public List<User> apply(List<UserEntity> input) {
                return entity2User(input);
            }
        });
    }

    public LiveData<List<User>> getContact(String userId, int pageNum) {
        return Transformations.map(database.contractDao().get(userId, PER_PAGE_NUM, pageNum * PER_PAGE_NUM), new Function<List<UserEntity>, List<User>>() {
            @Override
            public List<User> apply(List<UserEntity> input) {
                return entity2User(input);
            }
        });
    }

    public LiveData<User> getUserLive(String userId) {
        return Transformations.map(database.userDao().getLive(userId), new Function<UserEntity, User>() {
            @Override
            public User apply(UserEntity input) {
                User user = Config.defaultUser;
                if(input != null) {
                    user = entity2User(input);
                }
                return user;
            }
        });
    }

    private User getUser(String userId) {
        UserEntity entity = database.userDao().get(userId);
        User user = new User(entity.getId(), entity.getName(), entity.getAvatar());
        return user;
    }

    private Mic convert2Mic(MicEntity entity) {
        Topic topic = getTopic(entity.getTopicId());
        Mic mic = new Mic();
        mic.setTopic(topic);
        mic.setId(entity.getId());
        mic.setHasNew(entity.getHasNew());
        return mic;
    }

    public Mic getMic(String id) {
        MicEntity entity = database.micDao().get(id);
        Mic mic = new Mic();
        Topic topic = getTopic(entity.getTopicId());
        mic.setTopic(topic);
        mic.setId(entity.getId());
        mic.setHasNew(entity.getHasNew());
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

    private Topic getTopic(String id) {
        TopicEntity entity = database.topicDao().get(id);
        Topic topic = new Topic();
        topic.setId(entity.getId());
        topic.setTags(getTags(id));
        topic.setUserCan(getUserCan(id));
        topic.setName(entity.getName());
        topic.setBrief(entity.getName());
        topic.setSetting(new Setting("", entity.getSetting_url(), true));
        topic.setDerive_from(entity.getDerive_from());
        topic.setDialogue(getLine(entity.getId()));
        topic.setSponsor(getUser(entity.getSponsor()));
        topic.setMembers(getMembers(entity.getId()));
        return topic;
    }

    private Map<String, Set<CanOnTopic>> getUserCan(String topicId) {
        Map<String, Set<CanOnTopic>> userCans = new HashMap<>();
        List<TopicUserCanEntity> entities = database.topicUserCanDao().get(topicId);
        for(TopicUserCanEntity entity: entities) {
            if(userCans.containsKey(entity.getUserId())) {
                userCans.get(entity.getUserId()).add(CanOnTopic.valueOf(entity.getCan()));
            } else {
                Set<CanOnTopic> set = new LinkedHashSet<>();
                set.add(CanOnTopic.valueOf(entity.getCan()));
                userCans.put(entity.getUserId(), set);
            }
        }
        return userCans;
    }

    private Set<TopicTag> getTags(String topicId) {
        Set<TopicTag> tags = new LinkedHashSet<>();
        List<TopicTagEntity> entities = database.topicTagDao().get(topicId);
        for(TopicTagEntity entity: entities) {
            tags.add(TopicTag.valueOf(entity.getTag()));
        }
        return tags;
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
            topic.setSponsor(getUser(entity.getSponsor()));
            topic.setMembers(getMembers(entity.getId()));
        }
        return topics;
    }

    // endregion

    public void updateMicHasNew(MicHasNew micHasNew) {
        database.micDao().updateHasNew(micHasNew.getMicId(), micHasNew.isHasNew()? 1:0, new Date());
    }


}
