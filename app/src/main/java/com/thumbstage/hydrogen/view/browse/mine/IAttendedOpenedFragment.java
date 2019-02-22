package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IAttendedOpenedFragment extends Fragment {

    @BindView(R.id.fragment_recycler_common_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_recycler_common_recycler)
    RecyclerView recyclerView;

    BrowseViewModel viewModel;

    IAttendedOpenedAdapter recyclerViewAdapter;
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
        recyclerViewAdapter = new IAttendedOpenedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
        viewModel.getIAttendedOpened().observe(getActivity(), new Observer<List<TopicEx>>() {
            @Override
            public void onChanged(@Nullable List<TopicEx> topicExes) {
                recyclerViewAdapter.setItems(topicExes);
            }
        });
        viewModel.getIAttendedOpenedByPageNum(0);

        return view;
    }
}
