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

public abstract class ListDelegationAdapter extends PagedListAdapter<Mic, RecyclerView.ViewHolder> {

    List items = new ArrayList();
    protected AdapterDelegatesManager delegatesManager = new AdapterDelegatesManager();

    protected ListDelegationAdapter() {
        super(DIFF_CALLBACK);
    }

    protected ListDelegationAdapter(@NonNull DiffUtil.ItemCallback<Mic> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
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


    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Object object) {
        items.add(object);
        notifyItemChanged(items.size()-1);
    }
}
