package com.thumbstage.hydrogen.view.browse.atme;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;

import java.util.List;

public class AtMeAdapterDelegate implements IAdapterDelegate<List> {

    private int viewType;

    public AtMeAdapterDelegate(int viewType) {
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
                .inflate(R.layout.item_pipe_atme, parent, false);
        return new AtMeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((AtMeViewHolder)holder).setMic((Mic) items.get(position));
    }
    
}
