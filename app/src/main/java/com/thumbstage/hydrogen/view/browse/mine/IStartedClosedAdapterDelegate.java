package com.thumbstage.hydrogen.view.browse.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;

import java.util.List;

public class IStartedClosedAdapterDelegate implements IAdapterDelegate<List> {

    private int viewType;

    public IStartedClosedAdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public boolean isForViewType(@NonNull List items, int position) {
        return items.get(position) instanceof Mic;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic_browse, parent, false);
        return new IStartedClosedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((IStartedClosedViewHolder)holder).setMic((Mic) items.get(position));
    }
}
