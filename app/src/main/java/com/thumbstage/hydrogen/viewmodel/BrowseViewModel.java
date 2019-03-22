package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.repository.AtMeRepository;
import com.thumbstage.hydrogen.repository.MicRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BrowseViewModel extends ViewModel {

    private MicRepository micRepository;
    private AtMeRepository atMeRepository;

    final String TAG = "BrowseViewModel";

    @Inject
    public BrowseViewModel(MicRepository micRepository, AtMeRepository atMeRepository) {
        this.micRepository = micRepository;
        this.atMeRepository = atMeRepository;
    }

    public LiveData<List<Mic>> getIAttendedOpenedByPageNum(int pageNum) {
        return micRepository.getMic(TopicType.PICK_UP, UserGlobal.getInstance().getCurrentUserId(), false, pageNum);
    }

    public LiveData<List<Mic>> getPublishedOpenedByPageNum(int pageNum) {
        return micRepository.getMic(TopicType.PUBLISHED, "", false, pageNum);
    }

    public LiveData<List<Mic>> getIStartedOpenedByPageNum(int pageNum) {
        return micRepository.getMic(TopicType.UNPUBLISHED, UserGlobal.getInstance().getCurrentUserId(), false, pageNum);
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(String meId, int pageNum) {
        return atMeRepository.getAtMeByPageNum(meId, pageNum);
    }

}
