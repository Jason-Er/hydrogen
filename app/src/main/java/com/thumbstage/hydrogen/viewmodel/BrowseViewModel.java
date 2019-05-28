package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.app.Config;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.vo.Mic;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BrowseViewModel extends ViewModel {

    final String TAG = "BrowseViewModel";

    public LiveData<PagedList<Mic>> communityShowList;
    public LiveData<PagedList<Mic>> communityTopicList;
    public LiveData<PagedList<Mic>> iAttendedOpenedList;
    public LiveData<PagedList<Mic>> iAttendedClosedList;
    final CloudAPI cloudAPI;
    final ModelDB modelDB;
    final Executor executor;

    @Inject
    public BrowseViewModel(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(Config.PAGE_SIZE).setPrefetchDistance(Config.PREFETCH_DISTANCE).setEnablePlaceholders(false).build();

        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;

        initCommunityShowList(config);
        initCommunityTopicList(config);
        initIAttendedOpenedList(config);
        initIAttendedClosedList(config);

    }

    public void refreshCommunityShowList() {
        int pageNum = (int) communityShowList.getValue().getLastKey();
        cloudAPI.getMic(TopicTag.SELECTED, "", true, pageNum, new IReturnMicList() {
            @Override
            public void callback(final List<Mic> micList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        modelDB.saveMicList(micList);
                        communityShowList.getValue().getDataSource().invalidate();
                    }
                });
            }
        });
    }

    public void refreshCommunityTopicList() {
        int pageNum = (int) communityTopicList.getValue().getLastKey();
        cloudAPI.getMic(TopicTag.LITERAL, "", false, pageNum, new IReturnMicList() {
            @Override
            public void callback(final List<Mic> micList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        modelDB.saveMicList(micList);
                        communityTopicList.getValue().getDataSource().invalidate();
                    }
                });
            }
        });
    }

    public void refreshIAttendedOpenedList() {
        int pageNum = (int) iAttendedOpenedList.getValue().getLastKey();
        String userId = cloudAPI.getCurrentUser().getId();
        cloudAPI.getMic(TopicTag.SEMINAR, userId, false, pageNum, new IReturnMicList() {
            @Override
            public void callback(final List<Mic> micList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        modelDB.saveMicList(micList);
                        iAttendedOpenedList.getValue().getDataSource().invalidate();
                    }
                });
            }
        });
    }

    public void refreshIAttendedClosedList() {
        int pageNum = (int) iAttendedClosedList.getValue().getLastKey();
        String userId = cloudAPI.getCurrentUser().getId();
        cloudAPI.getMic(TopicTag.SEMINAR, userId, true, pageNum, new IReturnMicList() {
            @Override
            public void callback(final List<Mic> micList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        modelDB.saveMicList(micList);
                        iAttendedClosedList.getValue().getDataSource().invalidate();
                    }
                });
            }
        });
    }

    private void initCommunityShowList(PagedList.Config config) {
        communityShowList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.SELECTED, true), config)
                .setFetchExecutor(executor)
                .setInitialLoadKey(0)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        cloudAPI.getMic(TopicTag.SELECTED, "", true, 0, new IReturnMicList() {
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

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Mic itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Mic itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                    }
                }).build();
    }

    private void initCommunityTopicList(PagedList.Config config) {
        communityTopicList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.LITERAL, false), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        cloudAPI.getMic(TopicTag.LITERAL, "", false, 0, new IReturnMicList() {
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

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Mic itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Mic itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                    }
                }).build();
    }

    private void initIAttendedOpenedList(PagedList.Config config) {
        final String userId = cloudAPI.getCurrentUser().getId();
        iAttendedOpenedList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.SEMINAR, userId,false), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        cloudAPI.getMic(TopicTag.SEMINAR, userId, false, 0, new IReturnMicList() {
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

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Mic itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Mic itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                    }
                }).setInitialLoadKey(0).build();
    }

    private void initIAttendedClosedList(PagedList.Config config) {
        final String userId = cloudAPI.getCurrentUser().getId();
        iAttendedClosedList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.SEMINAR, userId,true), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        cloudAPI.getMic(TopicTag.SEMINAR, userId, true, 0, new IReturnMicList() {
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

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Mic itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Mic itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                    }
                }).build();
    }
    /*
    public LiveData<List<Mic>> getIAttendedOpenedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicTag.SEMINAR, userId, false, pageNum);
    }

    public LiveData<List<Mic>> getIAttendedClosedByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return topicRepository.getMic(TopicTag.SEMINAR, userId, true, pageNum);
    }

    public LiveData<List<Mic>> getCommunityTopicByPageNum(int pageNum) {
        return topicRepository.getMic(TopicTag.LITERAL, "", false, pageNum);
    }

    public LiveData<List<Mic>> getCommunityShowByPageNum(int pageNum) {
        return topicRepository.getMic(TopicTag.SELECTED, "", true, pageNum);
    }

    public void micHasNew(MicHasNew hasNew) {
        topicRepository.micHasNew(hasNew);
    }

    */


}
