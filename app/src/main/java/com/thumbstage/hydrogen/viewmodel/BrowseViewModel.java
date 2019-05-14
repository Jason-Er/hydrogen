package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.repository.TopicRepository;
import com.thumbstage.hydrogen.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BrowseViewModel extends ViewModel {

    private TopicRepository topicRepository;
    private UserRepository userRepository;

    final String TAG = "BrowseViewModel";

    @Inject
    public BrowseViewModel(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public LiveData<List<Mic>> getIAttendedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicTag.SEMINAR, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getIAttendedClosedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicTag.SEMINAR, userId, true, pageNum);
    }

    public LiveData<List<Mic>> getCommunityTopicByPageNum(int pageNum) {
        return topicRepository.getMic(TopicTag.LITERAL, "", false, pageNum);
    }

    public LiveData<List<Mic>> getCommunityShowByPageNum(int pageNum) {
        return topicRepository.getMic(TopicTag.SELECTED, "", true, pageNum);
    }

    public void saveIMMessage(IMMessage imMessage) {
        topicRepository.saveIMMessage(imMessage);
    }

    public void haveReadIMMessage(IMMessage imMessage) {
        topicRepository.haveReadIMMessage(imMessage);
    }
}
