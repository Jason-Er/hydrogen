package com.thumbstage.hydrogen.view.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;

import java.util.List;

public class ContactUserDelegate implements IAdapterDelegate<List> {

    private int viewType;

    public ContactUserDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public boolean isForViewType(@NonNull List items, int position) {
        return items.get(position) instanceof User;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_user, parent, false);
        return new ContactUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((ContactUserViewHolder)holder).setUser((User) items.get(position));
    }
    
}
