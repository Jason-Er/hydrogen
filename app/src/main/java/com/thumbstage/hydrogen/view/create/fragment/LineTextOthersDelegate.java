package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;
import com.thumbstage.hydrogen.view.create.type.LineEx;
import com.thumbstage.hydrogen.view.create.type.LineTextMine;
import com.thumbstage.hydrogen.view.create.type.LineTextOthers;

import java.util.List;

public class LineTextOthersDelegate implements IAdapterDelegate<List> {
    private int viewType;

    public LineTextOthersDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public boolean isForViewType(@NonNull List items, int position) {
        boolean status = false;
        Object object = items.get(position);
        if( object instanceof LineTextOthers) {
            status = true;
        }
        return status;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_line_left_layout, parent, false);
        ViewGroup contentContainer = view.findViewById(R.id.item_line_left_layout_content);
        View.inflate(view.getContext(), R.layout.item_line_left_text, contentContainer);
        return new LineTextOthersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((LineTextOthersViewHolder)holder).setLine((LineEx) items.get(position));
    }
}
