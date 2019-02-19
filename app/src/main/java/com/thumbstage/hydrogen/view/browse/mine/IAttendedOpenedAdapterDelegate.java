package com.thumbstage.hydrogen.view.browse.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.view.browse.IAdapterDelegate;

import java.util.List;

public class IAttendedOpenedAdapterDelegate implements IAdapterDelegate<List> {

    private int viewType;

    public IAttendedOpenedAdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public boolean isForViewType(@NonNull List items, int position) {
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {

    }
}
