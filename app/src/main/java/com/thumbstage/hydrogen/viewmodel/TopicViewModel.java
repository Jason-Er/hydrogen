package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
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

    public LiveData<Mic> getTheTopic() {
        return topicRepository.getTheMic();
    }

    public void createTheTopic(IReturnBool iReturnBool) {
        topicRepository.createTheMic(iReturnBool);
    }

    public void likeTheTopic(IReturnBool iReturnBool) {
        topicRepository.likeTheMic(iReturnBool);
    }

    public void refreshTheTopic() {
        topicRepository.refreshTheMic();
    }

    public void updateTheTopic(IReturnBool iReturnBool) {
        topicRepository.updateTheMic(iReturnBool);
    }

    public void speakLine(Line line, IReturnBool iReturnBool) {
        topicRepository.speakLine(line, iReturnBool);
    }

    public void closeTheTopic(final IReturnBool iReturnBool) {
        topicRepository.closeTheMic(iReturnBool);
    }

    public void updateSetting(IReturnBool iReturnBool) {
        topicRepository.updateSetting(iReturnBool);
    }

    public void updateBasicInfo(IReturnBool iReturnBool) {
        topicRepository.updateBasicInfo(iReturnBool);
    }

    public void updateMembers(IReturnBool iReturnBool) {
        topicRepository.updateMembers(iReturnBool);
    }

    public void updateMembers(List<User> users, IReturnBool iReturnBool) {
        topicRepository.updateMembers(users, iReturnBool);
    }

    public void micHasNew(MicHasNew hasNew) {
        topicRepository.micHasNew(hasNew);
    }

    public void micHasRead() {
        topicRepository.micHasRead();
    }

}
