package com.thumbstage.hydrogen.view.create.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.thumbstage.hydrogen.utils.DensityUtil;

public class SliderLayoutManager extends LinearLayoutManager {
    Context context;
    int position;
    public SliderLayoutManager(Context context) {
        super(context);
        this.context = context;
        position = 0;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state == RecyclerView.SCROLL_STATE_IDLE) {
            int recyclerViewCenterY = getRecyclerViewCenterY();
            int minDistance = DensityUtil.dp2px(context, 30);
            for(int i=0;i<getChildCount();i++) {
                View child = getChildAt(i);
                int childCenterY = getDecoratedTop(child) + (getDecoratedBottom(child) - getDecoratedTop(child)) / 2;
                int childDistanceFromCenter = Math.abs(childCenterY - recyclerViewCenterY);
                if(childDistanceFromCenter < minDistance) {
                    minDistance = childDistanceFromCenter;
                    position = i;
                }
            }
            Log.i("SliderLayoutManager", "onScrollStateChanged position: "+position);
        }
    }

    private int getRecyclerViewCenterY() {
        return DensityUtil.dp2px(context, 50)/2;
    }

    public int getPosition() {
        return position;
    }
}
