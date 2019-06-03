package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;
import com.thumbstage.hydrogen.view.create.type.LineAudioDirection;
import com.thumbstage.hydrogen.view.create.type.LineEx;
import com.thumbstage.hydrogen.view.create.type.LineTextDirection;

import java.util.List;

public class LineAudioDirectionDelegate implements IAdapterDelegate<List> {
    private int viewType;

    public LineAudioDirectionDelegate(int viewType) {
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
        if( object instanceof LineAudioDirection ) {
            status = true;
        }
        return status;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_line_center_layout, parent, false);
        ViewGroup contentContainer = view.findViewById(R.id.item_line_center_layout_content);
        View.inflate(view.getContext(), R.layout.item_line_left_audio, contentContainer);
        return new LineAudioDirectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {
        ((LineAudioDirectionViewHolder)holder).setLine((LineEx) items.get(position));
    }
}
