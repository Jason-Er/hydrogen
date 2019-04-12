package com.thumbstage.hydrogen.view.show.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ShowFragment extends Fragment {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_topic_bk)
    ImageView background;
    @BindView(R.id.fragment_chat_rv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;

    LinearLayoutManager layoutManager;
    ShowAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.bind(this, view);

        topicAdapter = new ShowAdapter();
        layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(topicAdapter);

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

    private void configureViewModel() {
        final String micId = getActivity().getIntent().getStringExtra(Mic.class.getSimpleName());
        topicViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopicViewModel.class);
        topicViewModel.pickUpTopic(micId).observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic mic) {
                topicAdapter.setMic(mic);
                if(mic.getTopic().getSetting() != null) {
                    Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                }
                spinner.setVisibility(View.GONE);
            }
        });
    }

}
