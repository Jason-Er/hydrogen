package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Log;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicType;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IReturnMic;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.callback.IStatusCallBack;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.create.type.LineTextRight;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public LiveData<Mic> pickUpMic(final String micId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getMic(micId, new IReturnMic() {
                    @Override
                    public void callback(Mic mic) {
                        micLiveData.setValue(mic);
                    }
                });
            }
        });
        return micLiveData;
    }

    public LiveData<Mic> editMic(final String micId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getMic(micId, new IReturnMic() {
                    @Override
                    public void callback(Mic mic) {
                        micLiveData.setValue(mic);
                    }
                });
            }
        });
        return micLiveData;
    }

    public void flushMicBuf(IReturnBool iReturnBool) {
        Mic mic = micLiveData.getValue();
        sendMicBuf(mic, iReturnBool);
    }

    private void sendMicBuf(Mic mic, final IReturnBool iReturnBool) {
        if(mic.getLineBuffer()!= null && mic.getLineBuffer().size()>0) {
            final Lock lock = new ReentrantLock();
            for(Line line: mic.getLineBuffer()) {
                lock.lock();
                cloudAPI.sendLine(mic, line, new IReturnBool() {
                    @Override
                    public void callback(Boolean status) {
                        if (status) {
                            lock.unlock();
                        }
                    }
                });
            }
            mic.getLineBuffer().clear();
            iReturnBool.callback(true);
        }
    }

    public void createTheMic(final TopicType type, final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                mic.getTopic().setType(type);
                cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        sendMicBuf(mic, new IReturnBool() {
                            @Override
                            public void callback(Boolean status) {
                                // modelDB.createTopic(t);
                                iReturnBool.callback(status);
                            }
                        });
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

    public void closeTheMic(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                cloudAPI.closeMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        if(!TextUtils.isEmpty(objectID)) {
                            iReturnBool.callback(true);
                        } else {
                            iReturnBool.callback(false);
                        }
                    }
                });
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
