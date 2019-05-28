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
import com.thumbstage.hydrogen.model.callback.IReturnMicList;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
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
    int communityShowListPageNum = 0;
    int communityTopicListPageNum = 0;
    int iAttendedOpenedListPageNum = 0;
    int iAttendedClosedListPageNum = 0;

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
        cloudAPI.getMic(TopicTag.SELECTED, "", true, 0, new IReturnMicList() {
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
        cloudAPI.getMic(TopicTag.LITERAL, "", false, 0, new IReturnMicList() {
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
        String userId = cloudAPI.getCurrentUser().getId();
        cloudAPI.getMic(TopicTag.SEMINAR, userId, false, 0, new IReturnMicList() {
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
        String userId = cloudAPI.getCurrentUser().getId();
        cloudAPI.getMic(TopicTag.SEMINAR, userId, true, 0, new IReturnMicList() {
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
                        // on the base that server page_size is the same with db page_size otherwise need calculation
                        communityShowListPageNum++;
                        cloudAPI.getMic(TopicTag.SELECTED, "", true, communityShowListPageNum, new IReturnMicList() {
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
                        // on the base that server page_size is the same with db page_size otherwise need calculation
                        communityTopicListPageNum++;
                        cloudAPI.getMic(TopicTag.LITERAL, "", false, communityTopicListPageNum, new IReturnMicList() {
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
                        // on the base that server page_size is the same with db page_size otherwise need calculation
                        iAttendedOpenedListPageNum++;
                        cloudAPI.getMic(TopicTag.SEMINAR, userId, false, iAttendedOpenedListPageNum, new IReturnMicList() {
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
                        // on the base that server page_size is the same with db page_size otherwise need calculation
                        iAttendedClosedListPageNum++;
                        cloudAPI.getMic(TopicTag.SEMINAR, userId, true, iAttendedClosedListPageNum, new IReturnMicList() {
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
                }).build();
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
