package com.thumbstage.hydrogen.view.browse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

public class AdapterDelegatesManager<T> {

    private SparseArray<IAdapterDelegate> delegates = new SparseArray<>();

    public AdapterDelegatesManager<T> addDelegate(@NonNull IAdapterDelegate<T> delegate) {
        delegates.put(delegate.getItemViewType(), delegate);
        return this;
    }

    public int getItemViewType(@NonNull T items, int position) {
        int key = 0;
        for(int i = 0; i < delegates.size(); i++) {
            key = delegates.keyAt(i);
            IAdapterDelegate<T> delegate = (IAdapterDelegate<T>)delegates.get(key);
            if ( delegate.isForViewType(items, position) ) {
                return key;
            }
        }
        throw new IllegalArgumentException("No delegate found");
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IAdapterDelegate<T> delegate = (IAdapterDelegate<T>) delegates.get(viewType);
        if (delegate != null) {
            return delegate.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No delegate found");
    }

    public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder viewHolder) {
        int key = 0;
        for(int i = 0; i < delegates.size(); i++) {
            key = delegates.keyAt(i);
            IAdapterDelegate<T> delegate = (IAdapterDelegate<T>)delegates.get(key);
            if ( delegate.isForViewType(items, position) ) {
                delegate.onBindViewHolder(items, position, viewHolder);
                return;
            }
        }
        throw new IllegalArgumentException("No delegate found");
    }
}
