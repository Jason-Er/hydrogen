package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;

import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.AtMe;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AtMeRepository {

    private ModelDB modelDB;
    private Executor executor;

    @Inject
    public AtMeRepository(ModelDB modelDB, Executor executor) {
        this.modelDB = modelDB;
        this.executor = executor;
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return modelDB.getAtMeByPageNum(meId, pageNum);
    }

    public void haveRead(final AtMe atMe) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                modelDB.deleteAtMe(atMe);
            }
        });
    }

}
