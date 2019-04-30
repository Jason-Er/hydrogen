package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.common.IAdapterDelegate;

import java.util.List;

public class LineTextCenterDelegate implements IAdapterDelegate<List> {
    private int viewType;

    public LineTextCenterDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public boolean isForViewType(@NonNull List items, int position) {
        boolean status = false;
        if( items.get(position) instanceof Line ) {
            if( !StringUtil.isUrl(((Line)items.get(position)).getWhat()) ) {
                status = false;
            }
        }
        return status;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic_browse, parent, false);
        return new LineTextLeftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List items, int position, @NonNull RecyclerView.ViewHolder holder) {

    }
}
