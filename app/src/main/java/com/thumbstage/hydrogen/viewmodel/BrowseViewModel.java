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

    @Inject
    public BrowseViewModel(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(Config.PAGE_SIZE).setPrefetchDistance(Config.PREFETCH_DISTANCE).setEnablePlaceholders(false).build();
        String userId = cloudAPI.getCurrentUser().getId();

        initCommunityShowList(config, cloudAPI, modelDB, executor);
        initCommunityTopicList(config, cloudAPI, modelDB, executor);
        initIAttendedOpenedList(userId, config, cloudAPI, modelDB, executor);
        initIAttendedClosedList(userId, config, cloudAPI, modelDB, executor);

    }

    private void initCommunityShowList(PagedList.Config config, final CloudAPI cloudAPI, final ModelDB modelDB, final Executor executor) {
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
                    }
                }).build();
    }

    private void initCommunityTopicList(PagedList.Config config, CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        communityTopicList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.LITERAL, false), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
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

    private void initIAttendedOpenedList(String userId, PagedList.Config config, CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        iAttendedOpenedList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.SEMINAR, userId,false), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
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

    private void initIAttendedClosedList(String userId, PagedList.Config config, CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        iAttendedClosedList = new LivePagedListBuilder<>(modelDB.getMic(TopicTag.SEMINAR, userId,true), config)
                .setFetchExecutor(executor)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Mic>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
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
