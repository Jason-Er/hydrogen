package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IReturnMic;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TopicRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    @Inject
    public TopicRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    public LiveData<List<Mic>> getMic(TopicTag tag, String userId, boolean isFinished, int pageNum) {
        refreshMicList(tag, userId, isFinished, pageNum);
        return modelDB.getMic(tag, userId, isFinished, pageNum);
    }

    private void refreshMicList(final TopicTag tag, final String userId, final boolean isFinished, final int pageNum) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if( modelDB.isTopicNeedFresh(tag, userId, isFinished) ) {
                    cloudAPI.getMic(tag, userId, isFinished, pageNum, new IReturnMicList() {
                        @Override
                        public void callback(final List<Mic> micList) {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    modelDB.saveMicList(micList);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private MutableLiveData<Mic> micLiveData = new MutableLiveData<>();

    public LiveData<Mic> createMic() {
        Mic mic = new Mic();
        mic.getTopic().getMembers().add(cloudAPI.getCurrentUser());
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
        refreshMic(micLiveData, micId);
        return micLiveData;
    }

    public LiveData<Mic> editMic(final String micId) {
        refreshMic(micLiveData, micId);
        return micLiveData;
    }

    public LiveData<Mic> getTheMic() {
        return micLiveData;
    }

    public void refreshTheMic() {
        refreshMic(micLiveData, micLiveData.getValue().getId());
    }

    private void refreshMic(final MutableLiveData<Mic> liveData, final String micId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getMic(micId, new IReturnMic() {
                    @Override
                    public void callback(final Mic mic) {
                        saveMic2DB(mic);
                        liveData.setValue(mic);
                    }
                });
            }
        });
    }

    public void updateTheMic(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                cloudAPI.updateMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        if(objectID.equals(mic.getId())) {
                            modelDB.saveMic(mic);
                            iReturnBool.callback(true);
                        } else {
                            iReturnBool.callback(false);
                        }
                    }
                });
            }
        });

    }

    public void createTheMic(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                    @Override
                    public void callback(String objectID) {
                        saveMic2DB(mic);
                        iReturnBool.callback(true);
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
                            saveMic2DB(mic);
                            iReturnBool.callback(true);
                        } else {
                            iReturnBool.callback(false);
                        }
                    }
                });
            }
        });
    }

    private void saveMic2DB(final Mic mic) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                modelDB.saveMic(mic);
            }
        });
    }

    public void updateMembers(final List<User> users, final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                if(!users.contains(cloudAPI.getCurrentUser())) {
                    users.add(cloudAPI.getCurrentUser());
                }

                mic.getTopic().setMembers(users);
                if(!TextUtils.isEmpty(mic.getId())) {
                    cloudAPI.updateMicMembers(mic, new IReturnBool() {
                        @Override
                        public void callback(Boolean isOK) {
                            if(isOK) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveMic2DB(mic);
                                    }
                                });
                                iReturnBool.callback(true);
                            }
                        }
                    });
                } else {
                    iReturnBool.callback(true);
                }
            }
        });
    }

    public void speakLine(Line line, IReturnBool iReturnBool) {
        final Mic mic = micLiveData.getValue();
        if(!TextUtils.isEmpty(mic.getId())) {
            cloudAPI.sendLine(mic, line, iReturnBool);
        }
    }

}
