package com.thumbstage.hydrogen.view.browse.published;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.Privilege;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.browse.IAdapterFunction;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class PublishedOpenedFragment extends Fragment implements IBrowseCustomize, IAdapterFunction {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.fragment_recycler_common_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_recycler_common_recycler)
    RecyclerView recyclerView;

    BrowseViewModel viewModel;

    PublishedOpenedAdapter recyclerViewAdapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_common, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerViewAdapter = new PublishedOpenedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureDagger();
        configureViewModel();
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseViewModel.class);
        viewModel.getPublishedOpened().observe(this, new Observer<List<TopicEx>>() {
            @Override
            public void onChanged(@Nullable List<TopicEx> topicExes) {
                recyclerViewAdapter.setItems(topicExes);
            }
        });
        viewModel.getPublishedOpenedByPageNum(0);
    }
    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(toolbar.getContext().getResources().getString(R.string.PublishedOpenedFragment_name));
    }

    @Override
    public void customizeFab( FloatingActionButton fab) {
        fab.hide();
    }
    // endregion

    // region implement of interface IAdapterFunction
    @Override
    public long getItemId() {
        return Privilege.BROWSE_PUBLISHEDOPENED.ordinal();
    }
    // endregion
}
