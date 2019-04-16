package com.thumbstage.hydrogen.view.browse.contact;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.AtMeEvent;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Privilege;
import com.thumbstage.hydrogen.view.browse.IAdapterFunction;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.view.browse.atme.AtMeAdapter;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ContactFragment extends BasicBrowseFragment implements IBrowseCustomize, IAdapterFunction {

    final String TAG = "ContactFragment";

    AtMeAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerViewAdapter = new AtMeAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(AtMeEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "click":
                viewModel.haveReadAtMe((AtMe) messageEvent.getData());
                break;
        }
    }

    @Override
    public void customObserve() {
        viewModel.getAtMeByPageNum(0)
                .observe(this, new Observer<List<AtMe>>() {
                    @Override
                    public void onChanged(@Nullable List<AtMe> atMe) {
                        recyclerViewAdapter.setItems(atMe);
                        spinner.setVisibility(View.GONE);
                    }
                });
    }

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(toolbar.getContext().getResources().getString(R.string.AtMeFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion

    // region implement of interface IAdapterFunction
    @Override
    public long getItemId() {
        return Privilege.BROWSE_CONTACT.ordinal();
    }
    // endregion

}
