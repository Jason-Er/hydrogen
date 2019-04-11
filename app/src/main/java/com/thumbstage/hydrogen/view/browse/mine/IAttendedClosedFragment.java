package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

import java.util.List;

public class IAttendedClosedFragment extends BasicBrowseFragment {

    IAttendedClosedAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new IAttendedClosedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void customObserve() {
        viewModel.getIAttendedClosedByPageNum(0).observe(this, new Observer<List<Mic>>() {
            @Override
            public void onChanged(@Nullable List<Mic> micList) {
                recyclerViewAdapter.setItems(micList);
                spinner.setVisibility(View.GONE);
            }
        });
    }
}
