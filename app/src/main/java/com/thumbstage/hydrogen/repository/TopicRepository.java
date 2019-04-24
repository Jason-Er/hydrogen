package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.bo.Line;
import com.thumbstage.hydrogen.model.bo.Mic;
import com.thumbstage.hydrogen.model.bo.TopicType;
import com.thumbstage.hydrogen.model.bo.User;
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

    public LiveData<List<Mic>> getMic(TopicType type, String started_by, boolean isFinished, int pageNum) {
        refreshMic(type, started_by, isFinished, pageNum);
        return modelDB.getMic(type, started_by, isFinished, pageNum);
    }

    private void refreshMic(final TopicType type, final String started_by, final boolean isFinished, final int pageNum) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if( modelDB.isTopicNeedFresh(type, started_by, isFinished) ) {
                    cloudAPI.getMic(type, started_by, isFinished, pageNum, new IReturnMicList() {
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

    public LiveData<Mic> getTheMic() {
        return micLiveData;
    }

    public void flushMicBuf(IReturnBool iReturnBool) {
        Mic mic = micLiveData.getValue();
        sendMicBuf(mic, iReturnBool);
        moveLineBuf2Dialogue(mic);
    }

    private void moveLineBuf2Dialogue(Mic mic) {
        mic.getTopic().getDialogue().addAll(mic.getLineBuffer());
        mic.getLineBuffer().clear();
    }

    private void sendMicBuf(final Mic mic, final IReturnBool iReturnBool) {
        if(mic.getLineBuffer()!= null && mic.getLineBuffer().size()>0) {
            for(final Line line: mic.getLineBuffer()) {
                cloudAPI.sendLine(mic, line, new IReturnBool() {
                    @Override
                    public void callback(Boolean status) {
                        if (status) {
                        }
                    }
                });
            }
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
                                moveLineBuf2Dialogue(mic);
                                saveMic(mic);
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
                            saveMic(mic);
                            iReturnBool.callback(true);
                        } else {
                            iReturnBool.callback(false);
                        }
                    }
                });
            }
        });
    }

    private void saveMic(final Mic mic) {
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
                                        saveMic(mic);
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


}
