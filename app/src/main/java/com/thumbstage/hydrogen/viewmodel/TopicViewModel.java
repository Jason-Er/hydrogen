package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.repository.TopicRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopicViewModel extends ViewModel {

    private TopicRepository topicRepository;

    @Inject
    public TopicViewModel(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public LiveData<Topic> getTopic() {
        return topicRepository.getTopic();
    }

    public void createTopic() {
        topicRepository.createTopic();
    }

    public void attendTopic(Topic publishedOpened) {
        topicRepository.attendTopic(publishedOpened);
    }

    public void continueTopic(Topic toContinue, Mic mic) {
        topicRepository.continueTopic(toContinue, mic);
    }

    public void addLine(Line line) {
        topicRepository.addLine(line);
    }


}
