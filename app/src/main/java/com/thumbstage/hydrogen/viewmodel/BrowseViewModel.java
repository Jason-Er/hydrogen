package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.repository.AtMeRepository;
import com.thumbstage.hydrogen.repository.TopicRepository;
import com.thumbstage.hydrogen.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BrowseViewModel extends ViewModel {

    private TopicRepository topicRepository;
    private AtMeRepository atMeRepository;
    private UserRepository userRepository;

    final String TAG = "BrowseViewModel";

    @Inject
    public BrowseViewModel(TopicRepository topicRepository, AtMeRepository atMeRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.atMeRepository = atMeRepository;
        this.userRepository = userRepository;
    }

    public LiveData<List<Mic>> getIAttendedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.PICK_UP, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getPublishedOpenedByPageNum(int pageNum) {
        return topicRepository.getMic(TopicType.PUBLISHED, "", false, pageNum);
    }

    public LiveData<List<Mic>> getIStartedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicType.UNPUBLISHED, userId, false, pageNum);
    }

    // region atMe
    public LiveData<List<AtMe>> getAtMeByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return atMeRepository.getAtMeByPageNum(userId, pageNum);
    }

    public void haveReadAtMe(AtMe atMe) {
        atMeRepository.haveRead(atMe);
    }

    // endregion

}
