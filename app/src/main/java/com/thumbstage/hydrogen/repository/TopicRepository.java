package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicType;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopicRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    @Inject
    public TopicRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    private MutableLiveData<Topic> topic = new MutableLiveData<>();

    public LiveData<Topic> getTopic() {
        return topic;
    }

    public void createTopic() {
        Topic topic = new Topic();
        this.topic.setValue(topic);
    }

    public void attendTopic(Topic publishedOpened) {

    }

    public void continueTopic(Mic mic) {

    }

    public void addLine(Line line) {

    }

    public void publishTheTopic() {
        executor.execute(new Runnable() {
             @Override
             public void run() {
                 Topic t = topic.getValue();
                 t.setType(TopicType.PUBLISHED);
                 cloudAPI.saveTopic(t);
                 modelDB.saveTopic(t);
             }
         });
    }

    public void startTheTopic() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Topic t = topic.getValue();
                t.setType(TopicType.UNPUBLISHED);
                cloudAPI.saveTopic(t);
                modelDB.saveTopic(t);
            }
        });
    }


}
