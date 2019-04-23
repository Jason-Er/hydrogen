package com.thumbstage.hydrogen.view.create.assist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TopicMemberFragment extends Fragment {

    @BindView(R.id.fragment_dialog_member_recycler)
    RecyclerView recyclerView;

    TopicMemberAdapter recyclerViewAdapter;
    GridLayoutManager layoutManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;
    TopicViewModel topicViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_topic_member, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewAdapter = new TopicMemberAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        configureViewModel();
    }

    private void configureViewModel(){
        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(UserViewModel.class);
        topicViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(TopicViewModel.class);
        topicViewModel.getTheTopic().observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic mic) {
                recyclerViewAdapter.setUsers(mic.getTopic().getMembers());
            }
        });
    }

    private void showMemberAvatars(@NonNull List<String> memberIds) {
        if(memberIds != null && !memberIds.isEmpty()) {
            userViewModel.getUsers(memberIds).observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {

                }
            });
        }
    }

}
