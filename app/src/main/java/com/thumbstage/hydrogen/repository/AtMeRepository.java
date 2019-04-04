package com.thumbstage.hydrogen.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.AtMeEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AtMeRepository {

    private HyDatabase database;
    private Executor executor;
    private MutableLiveData<List<AtMe>> atMeListLive;
    @Inject
    public AtMeRepository(HyDatabase database, Executor executor) {
        this.database = database;
        this.executor = executor;
        atMeListLive = new MutableLiveData<>();
    }

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
                            Mic mic = new Mic();
                            mic.setId(entity.getMicId());
                            atMe.setMic(mic);
                            atMe.setWhat(entity.getWhat());
                            atMe.setWhen(entity.getWhen());
                            UserEntity userEntity = database.userDao().get(entity.getWho());
                            User who = new User(userEntity.getId(), userEntity.getName(), userEntity.getAvatar());
                            atMe.setWho(who);
                            list.add(atMe);
                        }
                        atMeListLive.postValue(list);
                    }
                });
                return atMeListLive;
            }
        });
    }

}
