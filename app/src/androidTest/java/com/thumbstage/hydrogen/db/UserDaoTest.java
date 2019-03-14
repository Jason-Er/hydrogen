package com.thumbstage.hydrogen.db;

import android.support.test.runner.AndroidJUnit4;

import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserDaoTest extends DbTest {

    @Test
    public void insertAndLoad() throws InterruptedException {
        UserEntity userA = new UserEntity();
        userA.setId("1");
        userA.setName("A");
        userA.setAvatar("hello");
        userA.setLastRefresh(new Date());
        UserEntity userB = new UserEntity();
        userB.setId("2");
        userB.setName("B");
        userB.setAvatar("hello");
        userB.setLastRefresh(new Date());
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userA);
        userEntityList.add(userB);
        db.userDao().insert(userEntityList);

        UserEntity userEntity = db.userDao().get(userB.getName());

        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setName("1");
        topicEntity.setBrief("123");
        topicEntity.setId("3");
        topicEntity.setDerive_from("");
        topicEntity.setStarted_by("1");

        db.topicDao().insert(topicEntity);

        TopicEntity topic = db.topicDao().get(topicEntity.getId());




        assertThat(userEntity.getName(), is("B"));
    }
}
