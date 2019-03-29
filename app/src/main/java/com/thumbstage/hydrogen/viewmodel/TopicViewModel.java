package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.Mic;
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

    public LiveData<Mic> createTopic() {
        return topicRepository.createMic();
    }

    public LiveData<Mic> attendTopic(Mic mic) {
        return topicRepository.attendMic(mic);
    }

    public LiveData<Mic> pickUpTopic(Mic mic) {
        return topicRepository.pickUpMic(mic);
    }

    public void publishTheTopic() {
        topicRepository.publishTheTopic();
    }

    public void startTheTopic() {
        topicRepository.startTheTopic();
    }


}
