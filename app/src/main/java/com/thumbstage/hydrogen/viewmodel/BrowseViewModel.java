package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.repository.AtMeRepository;
import com.thumbstage.hydrogen.repository.TopicRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BrowseViewModel extends ViewModel {

    private TopicRepository topicRepository;
    private AtMeRepository atMeRepository;

    final String TAG = "BrowseViewModel";

    @Inject
    public BrowseViewModel(TopicRepository topicRepository, AtMeRepository atMeRepository) {
        this.topicRepository = topicRepository;
        this.atMeRepository = atMeRepository;
    }

    public LiveData<List<Mic>> getIAttendedOpenedByPageNum(String userId, int pageNum) {
        return topicRepository.getMic(TopicType.PICK_UP, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getPublishedOpenedByPageNum(int pageNum) {
        return topicRepository.getMic(TopicType.PUBLISHED, "", false, pageNum);
    }

    public LiveData<List<Mic>> getIStartedOpenedByPageNum(String userId, int pageNum) {
        return topicRepository.getMic(TopicType.UNPUBLISHED, userId, false, pageNum);
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String userId, int pageNum) {
        return atMeRepository.getAtMeByPageNum(userId, pageNum);
    }

}
