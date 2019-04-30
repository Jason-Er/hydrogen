package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

import java.util.List;

public class IPublishedOpenedFragment extends BasicBrowseFragment {

    IPublishedOpenedAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new IPublishedOpenedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void customObserve() {
        viewModel.getIPublishedOpenedByPageNum(0).observe(this, new Observer<List<Mic>>() {
            @Override
            public void onChanged(@Nullable List<Mic> micList) {
                recyclerViewAdapter.setItems(micList);
                spinner.setVisibility(View.GONE);
            }
        });
    }

}
