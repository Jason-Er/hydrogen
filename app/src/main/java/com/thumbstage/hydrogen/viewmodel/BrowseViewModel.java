package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.TopicInfo;

import java.util.ArrayList;
import java.util.List;

public class BrowseViewModel extends ViewModel {

    public final MutableLiveData<List<TopicInfo>> topicInfos = new MutableLiveData<>();

    public void getTopicInfos(int pageNum) {
        List<TopicInfo> list = new ArrayList<>();
        list.add(new TopicInfo("Topic one"));
        list.add(new TopicInfo("Topic two"));
        topicInfos.setValue(list);
    }
}
