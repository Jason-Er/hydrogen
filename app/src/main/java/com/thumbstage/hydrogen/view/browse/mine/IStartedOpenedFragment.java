package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;
import com.thumbstage.hydrogen.view.browse.BrowseRecyclerViewAdapter;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

public class IStartedOpenedFragment extends Fragment implements BrowseCustomize {

    private BrowseViewModel viewModel;
    private BrowseRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.list_browse, container, false);
        rootView.setLayoutManager(new LinearLayoutManager(getContext()));
        rootView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new BrowseRecyclerViewAdapter();
        rootView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
        viewModel.getPublishedOpenedTopicInfos().observe(getActivity(), new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                adapter.setTopics(topics);
            }
        });
        viewModel.getPublishedOpenedTopicInfosByPageNum(0);
        return rootView;
    }

    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.show();
        fab.setImageResource(R.drawable.ic_button_plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    // endregion
}
