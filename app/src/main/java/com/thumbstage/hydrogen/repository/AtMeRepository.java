package com.thumbstage.hydrogen.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.AtMeEntity;
import com.thumbstage.hydrogen.model.AtMe;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AtMeRepository {

    private final HyDatabase database;

    @Inject
    public AtMeRepository(HyDatabase database) {
        this.database = database;
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return Transformations.map(database.atMeDao().get(meId, 15, pageNum * 15), new Function<List<AtMeEntity>, List<AtMe>>() {
            @Override
            public List<AtMe> apply(List<AtMeEntity> input) {
                Log.i("AtMeRepository", "getAtMeByPageNum");
                return null;
            }
        });
    }

}
