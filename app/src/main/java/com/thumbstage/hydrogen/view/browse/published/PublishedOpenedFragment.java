package com.thumbstage.hydrogen.view.browse.published;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicInfo;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

public class PublishedOpenedFragment extends Fragment {

    private BrowseViewModel viewModel;
    private PublishedOpenedRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.list_browse, container, false);
        rootView.setLayoutManager(new LinearLayoutManager(getContext()));
        rootView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new PublishedOpenedRecyclerViewAdapter();
        rootView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
        viewModel.topicInfos.observe(getActivity(), new Observer<List<TopicInfo>>() {
            @Override
            public void onChanged(@Nullable List<TopicInfo> topicInfos) {
                adapter.setTopicInfos(topicInfos);
            }
        });
        viewModel.getTopicInfos(0);

        return rootView;
    }
}
