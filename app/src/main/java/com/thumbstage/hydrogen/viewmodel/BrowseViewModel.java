package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.thumbstage.hydrogen.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class BrowseViewModel extends ViewModel {

    private final MutableLiveData<List<Topic>> publishedOpenedTopicInfos = new MutableLiveData<>();

    public void getPublishedOpenedTopicInfosByPageNum(int pageNum) {
        AVQuery<AVObject> avQuery = new AVQuery<>("Topic");
        avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("brief");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    List<Topic> list = new ArrayList<>();
                    for(AVObject avObject : avObjects) {
                        list.add(new Topic(avObject.getString("name"),
                                avObject.getString("brief"),
                                avObject.getString("sponsor_name")));
                    }
                    publishedOpenedTopicInfos.setValue(list);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<List<Topic>> getPublishedOpenedTopicInfos() {
        return publishedOpenedTopicInfos;
    }
}
