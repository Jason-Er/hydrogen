package com.thumbstage.hydrogen.view.show.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShowLayoutManager extends RecyclerView.LayoutManager {

    int decoratedChildWidth, decoratedChildHeight;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getItemCount() == 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        View firstView = recycler.getViewForPosition(0);
        addView(firstView);
        measureChildWithMargins(firstView, 0, 0);
        decoratedChildWidth = getDecoratedMeasuredWidth(firstView);
        decoratedChildHeight = getDecoratedMeasuredHeight(firstView);
        switch (getItemCount()) {
            case 1:
                layoutOneParticipant(recycler);
                break;
            case 2:
                layoutTwoParticipant(recycler);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void layoutOneParticipant(RecyclerView.Recycler recycler) {
        View view = getChildAt(0);
        Rect center = new Rect(
                (getHorizontalSpace() - decoratedChildWidth)/2,
                (getVerticalSpace() - decoratedChildHeight)/2,
                (getHorizontalSpace() + decoratedChildWidth)/2,
                (getVerticalSpace() + decoratedChildHeight)/2
        );
        layoutDecorated(view, center.left, center.top, center.right, center.bottom);

    }
    private void layoutTwoParticipant(RecyclerView.Recycler recycler) {
        View view = getChildAt(0);
        Rect center = new Rect(
                getHorizontalSpace()/3 - decoratedChildWidth/2,
                (getVerticalSpace() - decoratedChildHeight)/2,
                getHorizontalSpace()/3 + decoratedChildWidth/2,
                (getVerticalSpace() + decoratedChildHeight)/2
        );
        View view2 = recycler.getViewForPosition(1);
        addView(view2);
        Rect center2 = new Rect(
                getHorizontalSpace()*2/3 - decoratedChildWidth/2,
                (getVerticalSpace() - decoratedChildHeight)/2,
                getHorizontalSpace()*2/3 + decoratedChildWidth/2,
                (getVerticalSpace() + decoratedChildHeight)/2
        );
        layoutDecorated(view, center.left, center.top, center.right, center.bottom);
        layoutDecorated(view2, center2.left, center2.top, center2.right, center2.bottom);
    }
    private void layoutThreeParticipant(RecyclerView.Recycler recycler) {

    }
    private void layoutFourParticipant(RecyclerView.Recycler recycler) {

    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingStart() - getPaddingEnd();
    }

}
