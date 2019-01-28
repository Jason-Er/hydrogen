package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.thumbstage.hydrogen.model.TopicInfo;

import java.util.ArrayList;
import java.util.List;

public class BrowseViewModel extends ViewModel {

    public final MutableLiveData<List<TopicInfo>> topicInfos = new MutableLiveData<>();

    public void getTopicInfos(int pageNum) {
        AVQuery<AVObject> avQuery = new AVQuery<>("TopicInfo");
        avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("brief");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    List<TopicInfo> list = new ArrayList<>();
                    for(AVObject avObject : avObjects) {
                        list.add(new TopicInfo(avObject.getString("name"), avObject.getString("brief")));
                    }
                    topicInfos.setValue(list);
                } else {
                    avException.printStackTrace();
                }
            }
        });
    }
}
