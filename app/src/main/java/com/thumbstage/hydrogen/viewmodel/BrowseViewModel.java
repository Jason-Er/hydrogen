package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.bo.TopicType;
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
        return topicRepository.getMic(TopicType.PICK_UP, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getIAttendedClosedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.PICK_UP, userId, true, pageNum);
    }

    public LiveData<List<Mic>> getPublishedOpenedByPageNum(int pageNum) {
        return topicRepository.getMic(TopicType.PUBLISHED, "", false, pageNum);
    }

    public LiveData<List<Mic>> getPublishedClosedByPageNum(int pageNum) {
        return topicRepository.getMic(TopicType.PUBLISHED, "", true, pageNum);
    }

    public LiveData<List<Mic>> getIPublishedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.PUBLISHED, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getIPublishedClosedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.PUBLISHED, userId, true, pageNum);
    }

    public LiveData<List<Mic>> getIStartedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.UNPUBLISHED, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getIStartedClosedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.UNPUBLISHED, userId, true, pageNum);
    }

}
