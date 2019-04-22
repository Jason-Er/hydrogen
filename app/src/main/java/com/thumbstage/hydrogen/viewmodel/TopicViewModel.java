package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.repository.TopicRepository;

import java.io.File;
import java.util.List;

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

    public LiveData<Mic> attendTopic(String micId) {
        return topicRepository.attendMic(micId);
    }

    public LiveData<Mic> pickUpTopic(String micId) {
        return topicRepository.pickUpMic(micId);
    }

    public LiveData<Mic> editTopic(String micId) {
        return topicRepository.editMic(micId);
    }

    public void saveFile(File file, IReturnHyFile iReturnHyFile) {
        topicRepository.saveFile(file, iReturnHyFile);
    }

    public void createTheTopic(TopicType type, IReturnBool iReturnBool) {
        topicRepository.createTheMic(type, iReturnBool);
    }

    public void flushMicBuf(IReturnBool iReturnBool) {
        topicRepository.flushMicBuf(iReturnBool);
    }

    public void closeTheTopic(final IReturnBool iReturnBool) {
        topicRepository.closeTheMic(iReturnBool);
    }

    public void updateMembers(List<User> users, IReturnBool iReturnBool) {
        topicRepository.updateMembers(users, iReturnBool);
    }


}
