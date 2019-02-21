package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import cn.leancloud.chatkit.viewholder.LCIMConversationItemHolder;


public class IStartedOpenedFragment extends Fragment implements IBrowseCustomize {

    @BindView(R.id.fragment_conversation_srl_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_conversation_srl_view)
    RecyclerView recyclerView;

    BrowseViewModel viewModel;

    LCIMCommonListAdapter<AVIMConversation> itemAdapter;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lcim_conversation_list_fragment, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
        itemAdapter = new LCIMCommonListAdapter<AVIMConversation>(LCIMConversationItemHolder.class);
        recyclerView.setAdapter(itemAdapter);

        viewModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
        viewModel.getIStartedOpened().observe(getActivity(), new Observer<List<TopicEx>>() {
            @Override
            public void onChanged(@Nullable List<TopicEx> topicExes) {
                // itemAdapter.setDataList(conversations);
                // itemAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getIStartedOpenedByPageNum(0);

        return view;
    }

    // region implement of interface IBrowseCustomize
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
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                intent.putExtra(CreateActivity.TopicHandleType.class.getSimpleName(),
                        CreateActivity.TopicHandleType.CREATE.name());
                startActivity(intent);
            }
        });
    }
    // endregion
}
