package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

public class IAttendedOpenedFragment extends BasicBrowseFragment {

    IAttendedOpenedAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new IAttendedOpenedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void customObserve() {
        viewModel.iAttendedOpenedList.observe(this, new Observer<PagedList<Mic>>() {
            @Override
            public void onChanged(@Nullable PagedList<Mic> micList) {
                recyclerViewAdapter.submitList(micList);
                spinner.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void swipeRefresh() {
        viewModel.refreshIAttendedOpenedList();
    }

}
