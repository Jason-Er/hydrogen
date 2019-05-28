package com.thumbstage.hydrogen.view.common;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.view.browse.AdapterDelegatesManager;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedListDelegationAdapter extends PagedListAdapter<Mic, RecyclerView.ViewHolder> {

    protected AdapterDelegatesManager delegatesManager = new AdapterDelegatesManager();

    protected PagedListDelegationAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(getCurrentList(), position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(getCurrentList(), position);
    }

    private static DiffUtil.ItemCallback<Mic> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Mic>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Mic oldMic, Mic newMic) {
                    return oldMic.getId() == newMic.getId();
                }

                @Override
                public boolean areContentsTheSame(Mic oldMic,
                                                  Mic newMic) {
                    return oldMic.equals(newMic);
                }
            };

}
