package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;

import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.AtMe;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AtMeRepository {

    private ModelDB modelDB;

    @Inject
    public AtMeRepository(ModelDB modelDB) {
        this.modelDB = modelDB;
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return modelDB.getAtMeByPageNum(meId, pageNum);
    }

}
