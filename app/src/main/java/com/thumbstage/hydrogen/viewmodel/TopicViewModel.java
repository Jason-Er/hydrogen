package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.repository.MicRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopicViewModel extends ViewModel {

    private MicRepository micRepository;

    @Inject
    public TopicViewModel(MicRepository micRepository) {
        this.micRepository = micRepository;
    }

    public LiveData<Mic> createTopic() {
        return micRepository.createMic();
    }

    public LiveData<Mic> attendTopic(Mic mic) {
        return micRepository.attendMic(mic);
    }

    public LiveData<Mic> pickUpTopic(Mic mic) {
        return micRepository.pickUpMic(mic);
    }

    /*
    public void addLine(Line line) {
        micRepository.addLine(line);
    }
    */

    public void publishTheTopic() {
        micRepository.publishTheTopic();
    }

    public void startTheTopic() {
        micRepository.startTheTopic();
    }


}
