package com.thumbstage.hydrogen.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.bo.HyFile;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnHyFile;
import com.thumbstage.hydrogen.model.callback.IReturnMicDto;
import com.thumbstage.hydrogen.model.callback.IReturnTopicDto;
import com.thumbstage.hydrogen.model.dto.MicDto;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
import com.thumbstage.hydrogen.model.dto.TopicDto;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;

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

    private MutableLiveData<Mic> micLiveData = new MutableLiveData<>();

    public LiveData<Mic> createMic() {
        Mic mic = new Mic();
        mic.getTopic().getMembers().add(cloudAPI.getCurrentUser());
        micLiveData.setValue(mic);
        return micLiveData;
    }

    public LiveData<Mic> attendMic(String micId) {
        refreshMic(micId);
        return Transformations.switchMap(modelDB.getMicLive(micId), new Function<Mic, LiveData<Mic>>() {
            @Override
            public LiveData<Mic> apply(Mic input) {
                micLiveData.setValue(input==null? null:(Mic)input.clone());
                return micLiveData;
            }
        });
    }

    public LiveData<Mic> pickUpMic(String micId) {
        refreshMic(micId);
        return Transformations.switchMap(modelDB.getMicLive(micId), new Function<Mic, LiveData<Mic>>() {
            @Override
            public LiveData<Mic> apply(Mic input) {
                micLiveData.setValue(input);
                return micLiveData;
            }
        });
    }

    public LiveData<Mic> getTheMic() {
        return micLiveData;
    }

    public void forceRefreshTopic(final String topicId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getTopicDto(topicId, new IReturnTopicDto() {
                    @Override
                    public void callback(final TopicDto topic) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                modelDB.saveTopicDto(topic);
                            }
                        });
                    }
                });
            }
        });
    }

    private void refreshMic(final String micId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if( modelDB.isMicNeedFresh(micId) ) {
                    cloudAPI.getMicDto(micId, new IReturnMicDto() {
                        @Override
                        public void callback(final MicDto mic) {
                            Log.i("TopicRepository", "refreshMic getMicDto");
                            saveMic2DB(mic);
                        }
                    });
                } else {
                    List<String> userIds = modelDB.getNeedFreshMicMembers(micId);
                    if(!userIds.isEmpty()) {
                        cloudAPI.getUsers(userIds, new CloudAPI.IReturnUsers() {
                            @Override
                            public void callback(final List<User> users) {
                                Log.i("TopicRepository", "refreshMic getUsers");
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        modelDB.saveUserList(users);
                                        modelDB.updateMicLastRefresh(micId);
                                    }
                                });
                            }
                        });
                    }
                }
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
                if (mic.getTopic().getSetting() != null && !URLUtil.isValidUrl(mic.getTopic().getSetting())) { // then is local file
                    File file = new File(mic.getTopic().getSetting());
                    cloudAPI.saveFile(file, new IReturnHyFile() {
                        @Override
                        public void callback(HyFile hyFile) {
                            mic.getTopic().setSetting(hyFile.getUrl());
                            cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                                @Override
                                public void callback(String objectID) {
                                    saveMic2DB(mic);
                                    iReturnBool.callback(true);
                                }
                            });
                        }
                    });
                } else {
                    cloudAPI.createMic(mic, new CloudAPI.ICallBack() {
                        @Override
                        public void callback(String objectID) {
                            saveMic2DB(mic);
                            iReturnBool.callback(true);
                        }
                    });
                }
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

    private void saveMic2DB(final Mic mic) { // from UI side
        executor.execute(new Runnable() {
            @Override
            public void run() {
                modelDB.saveMic(mic);
            }
        });
    }

    private void saveMic2DB(final MicDto mic) { // for CloudAPI
        executor.execute(new Runnable() {
            @Override
            public void run() {
                modelDB.saveMicDto(mic);
            }
        });
    }

    public void updateSetting(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                if(!TextUtils.isEmpty(mic.getId())) {
                    File file = new File(mic.getTopic().getSetting());
                    cloudAPI.saveFile(file, new IReturnHyFile() {
                        @Override
                        public void callback(HyFile hyFile) {
                            mic.getTopic().setSetting(hyFile.getUrl());
                            cloudAPI.updateTopicSetting(mic.getTopic(), new IReturnBool() {
                                @Override
                                public void callback(Boolean isOK) {
                                    if(isOK) {
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                modelDB.saveMic(mic);
                                            }
                                        });
                                        iReturnBool.callback(true);
                                    } else {
                                        iReturnBool.callback(false);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    iReturnBool.callback(true);
                }
            }
        });
    }

    public void updateBasicInfo(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                if(!TextUtils.isEmpty(mic.getId())) {
                    cloudAPI.updateTopicInfo(mic.getTopic(), new IReturnBool() {
                        @Override
                        public void callback(Boolean isOK) {
                            if(isOK) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        modelDB.saveMic(mic);
                                    }
                                });
                                iReturnBool.callback(true);
                            } else {
                                iReturnBool.callback(false);
                            }
                        }
                    });
                } else {
                    iReturnBool.callback(true);
                }
            }
        });
    }

    public void updateMembers(final IReturnBool iReturnBool) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Mic mic = micLiveData.getValue();
                if(!TextUtils.isEmpty(mic.getId())) {
                    cloudAPI.updateMicMembers(mic, new IReturnBool() {
                        @Override
                        public void callback(Boolean isOK) {
                            if(isOK) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        modelDB.saveMic(mic);
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

    public void speakLine(final Line line, final IReturnBool iReturnBool) {
        final Mic mic = micLiveData.getValue();
        if(!TextUtils.isEmpty(mic.getId())) {
            cloudAPI.sendLine(mic, line, new IReturnBool() {
                @Override
                public void callback(Boolean isOK) {
                    if(isOK) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                modelDB.saveLine(line, mic.getTopic().getId());
                            }
                        });
                    }
                    iReturnBool.callback(isOK);
                }
            });
        }
    }

    public void micHasNew(final MicHasNew hasNew) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                modelDB.updateMicHasNew(hasNew);
            }
        });
    }

}
