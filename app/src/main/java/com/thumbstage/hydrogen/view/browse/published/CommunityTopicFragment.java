package com.thumbstage.hydrogen.view.browse.published;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.view.browse.IAdapterFunction;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

import java.util.List;

public class CommunityTopicFragment extends BasicBrowseFragment implements IBrowseCustomize, IAdapterFunction {

    CommunityTopicAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new CommunityTopicAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void customObserve() {
        viewModel.communityTopicList.observe(this, new Observer<PagedList<Mic>>() {
            @Override
            public void onChanged(@Nullable PagedList<Mic> micList) {
                recyclerViewAdapter.submitList(micList);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void swipeRefresh() {
        /*
        viewModel.getCommunityTopicByPageNum(0).observe(this, new Observer<List<Mic>>() {
            @Override
            public void onChanged(@Nullable List<Mic> micList) {
                recyclerViewAdapter.setItems(micList);
                refreshLayout.setRefreshing(false);
            }
        });
        */
    }

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(toolbar.getContext().getResources().getString(R.string.PublishedOpenedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion

    // region implement of interface IAdapterFunction
    @Override
    public long getItemId() {
        return Privilege.BROWSE_COMMUNITY_TOPIC.ordinal();
    }
    // endregion
}
