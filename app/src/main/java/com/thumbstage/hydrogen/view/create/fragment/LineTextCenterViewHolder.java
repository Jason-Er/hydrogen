package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thumbstage.hydrogen.model.vo.Line;

import butterknife.ButterKnife;

public class LineTextCenterViewHolder extends RecyclerView.ViewHolder {

    Line line;

    public LineTextCenterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
