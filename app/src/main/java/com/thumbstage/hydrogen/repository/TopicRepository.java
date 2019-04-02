package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Log;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IReturnMic;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.callback.IStatusCallBack;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TopicRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    private Map<String, MutableLiveData<List<Mic>>> liveDataMap = new HashMap<>();

    @Inject
    public TopicRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    public LiveData<List<Mic>> getMic(TopicType type, String started_by, boolean isFinished, int pageNum) {
        MutableLiveData<List<Mic>> micListLive;
        String key = type.name()+isFinished;
        if(!TextUtils.isEmpty(started_by)) {
            key += "personal";
        }
        if(liveDataMap.containsKey(key)) {
            micListLive = liveDataMap.get(key);
        } else {
            micListLive = new MutableLiveData<>();
            liveDataMap.put(key, micListLive);
        }
        getMic(type, started_by, isFinished, pageNum, micListLive);
        return micListLive;
    }

    private void getMic(final TopicType type, final String started_by, final boolean isFinished, final int pageNum, final MutableLiveData<List<Mic>> mutableLiveData) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if( modelDB.isTopicNeedFresh(type) ) {
                    cloudAPI.getMic(type, started_by, isFinished, pageNum, new IReturnMicList() {
                        @Override
                        public void callback(final List<Mic> micList) {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    List<Topic> topics = new ArrayList<>();
                                    for(Mic mic:micList) {
                                        topics.add(mic.getTopic());
                                    }
                                    modelDB.saveTopicList(topics);
                                    modelDB.saveMicList(micList);
                                    mutableLiveData.postValue(micList);
                                }
                            });
                        }
                    });
                } else {
                    // TODO: 3/15/2019 perPageNum need refactoring
                    List<Mic> micList = modelDB.getMicByPage(type, started_by, pageNum, 15);
                    mutableLiveData.postValue(micList);
                }
            }
        });
    }

    private MutableLiveData<Mic> micLiveData = new MutableLiveData<>();

    public LiveData<Mic> createMic() {
        Mic mic = new Mic();
        micLiveData.setValue(mic);
        return micLiveData;
    }

    public LiveData<Mic> attendMic(final String micId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getMic(micId, new IReturnMic() {
                    @Override
                    public void callback(Mic mic) {
                        Mic copy = (Mic) mic.clone();
                        micLiveData.setValue(copy);
                    }
                });
            }
        });
        return micLiveData;
    }

    public LiveData<Mic> pickUpMic(String micId) {
        // TODO: 3/22/2019 do something
        return micLiveData;
    }

    public void publishTheTopic(final IStatusCallBack iStatusCallBack) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Mic mic = micLiveData.getValue();
                mic.getTopic().setType(TopicType.PUBLISHED);
                cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        Log.i("TopicRepository","publishTheTopic ok objectID: "+objectID);
                        // modelDB.saveTopic(t);
                        iStatusCallBack.callback(IStatusCallBack.STATUS.OK);
                    }
                });
            }
        });
    }

    public void startTheTopic(final IStatusCallBack iStatusCallBack) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Mic mic = micLiveData.getValue();
                mic.getTopic().setType(TopicType.UNPUBLISHED);
                cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        Log.i("TopicRepository","startTheTopic ok objectID: "+objectID);
                        // modelDB.saveTopic(t);
                        iStatusCallBack.callback(IStatusCallBack.STATUS.OK);
                    }
                });
            }
        });
    }

    public void saveFile(final File file, final IReturnHyFile iReturnHyFile) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.saveFile(file, iReturnHyFile);
            }
        });
    }

    /*
    public void setListenIMCallBack(IMMessageHandler imMessageHandler) {
        imMessageHandler.setCallback(new IIMCallBack() {
            @Override
            public void callBack() {
                updateConversationList();
            }
        });
    }

    public void setListenIMCallBack(IMConversationHandler imConversationHandler) {
        imConversationHandler.setCallback(new IIMCallBack() {
            @Override
            public void callBack() {
                updateConversationList();
            }
        });
    }

    private void updateConversationList() {
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        List<Mic> micList = new ArrayList<>();
        // List<AVIMConversation> conversationList = new ArrayList<>();
        for (String convId : convIdList) {
            micList.add(new Mic(convId));
            // conversationList.add(CurrentUser.getInstance().getClient().getConversation(convId));
        }
        atMe.setValue(micList);
    }
    */
}
