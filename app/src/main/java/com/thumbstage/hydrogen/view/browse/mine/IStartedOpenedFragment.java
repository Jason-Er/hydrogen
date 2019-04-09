package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.fragment.TopicHandleType;

import java.util.List;

public class IStartedOpenedFragment extends BasicBrowseFragment {

    IStartedOpenedAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new IStartedOpenedAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void customObserve() {
        viewModel.getIStartedOpenedByPageNum(0).observe(this, new Observer<List<Mic>>() {
            @Override
            public void onChanged(@Nullable List<Mic> micList) {
                recyclerViewAdapter.setItems(micList);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(final FloatingActionButton fab) {
        fab.show();
        fab.setImageResource(R.drawable.ic_button_plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fab.getContext(), CreateActivity.class);
                intent.putExtra(TopicHandleType.class.getSimpleName(),
                        TopicHandleType.CREATE.name());
                fab.getContext().startActivity(intent);
            }
        });
    }
    // endregion

}
