package com.thumbstage.hydrogen.view.common;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public abstract class BasicBrowseFragment extends Fragment {

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.fragment_recycler_common_pullrefresh)
    protected SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_recycler_common_recycler)
    protected RecyclerView recyclerView;
    @BindView(R.id.loading_spinner)
    protected ProgressBar spinner;

    protected BrowseViewModel viewModel;
    protected LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_common, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
        spinner.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        customObserve();
    }

    abstract public void customObserve();
    abstract public void swipeRefresh();

}
