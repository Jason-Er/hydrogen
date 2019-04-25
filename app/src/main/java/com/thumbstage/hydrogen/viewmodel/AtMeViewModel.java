package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.bo.AtMe;
import com.thumbstage.hydrogen.repository.AtMeRepository;
import com.thumbstage.hydrogen.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AtMeViewModel extends ViewModel {
    private UserRepository userRepository;
    private AtMeRepository atMeRepository;

    @Inject
    public AtMeViewModel(UserRepository userRepository, AtMeRepository atMeRepository) {
        this.userRepository = userRepository;
        this.atMeRepository = atMeRepository;
    }

    public LiveData<List<AtMe>> getAtMeByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return atMeRepository.getAtMeByPageNum(userId, pageNum);
    }

    public void haveReadAtMe(AtMe atMe) {
        atMeRepository.haveRead(atMe);
    }

    public void saveAtMe(AtMe atMe) {
        atMeRepository.saveAtMe(atMe);
    }
}
