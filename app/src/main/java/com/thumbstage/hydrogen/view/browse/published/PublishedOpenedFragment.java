package com.thumbstage.hydrogen.view.browse.published;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import cn.leancloud.chatkit.viewholder.LCIMConversationItemHolder;

public class PublishedOpenedFragment extends Fragment implements BrowseCustomize {

    @BindView(R.id.fragment_conversation_srl_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_conversation_srl_view)
    RecyclerView recyclerView;

    BrowseViewModel viewModel;

    LCIMCommonListAdapter<AVIMConversation> itemAdapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lcim_conversation_list_fragment, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
        itemAdapter = new LCIMCommonListAdapter<AVIMConversation>(LCIMConversationItemHolder.class);
        recyclerView.setAdapter(itemAdapter);

        viewModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
        viewModel.getPublishedOpened().observe(getActivity(), new Observer<List<AVIMConversation>>() {
            @Override
            public void onChanged(@Nullable List<AVIMConversation> conversations) {
                itemAdapter.setDataList(conversations);
                itemAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getPublishedOpenedByPageNum(0);

        return view;
    }

    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.PublishedOpenedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
