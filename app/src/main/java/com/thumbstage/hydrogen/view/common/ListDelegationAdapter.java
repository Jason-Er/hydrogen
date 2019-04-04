package com.thumbstage.hydrogen.view.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.view.browse.AdapterDelegatesManager;

import java.util.ArrayList;
import java.util.List;

public class ListDelegationAdapter extends RecyclerView.Adapter {

    List items = new ArrayList();
    protected AdapterDelegatesManager delegatesManager = new AdapterDelegatesManager();

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

    public void setItems(List items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Object object) {
        items.add(object);
        notifyItemChanged(items.size()-1);
    }
}
