package com.thumbstage.hydrogen.view.show.fragment;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShowLayoutManager extends RecyclerView.LayoutManager {

    int decoratedChildWidth, decoratedChildHeight;
    Point center;

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

        center = new Point(getHorizontalSpace()/2,getVerticalSpace()/2);

        View firstView = recycler.getViewForPosition(0);
        addView(firstView);
        measureChildWithMargins(firstView, 0, 0);
        decoratedChildWidth = getDecoratedMeasuredWidth(firstView);
        decoratedChildHeight = getDecoratedMeasuredHeight(firstView);

        switch (getItemCount()) {
            case 1:
                layoutParticipantCircle(recycler, 0, 0);
                break;
            case 2:
                layoutParticipantCircle(recycler, 0, getHorizontalSpace()/6);
                break;
            case 3:
                layoutParticipantCircle(recycler, -90, getVerticalSpace()/2 - decoratedChildHeight);
                break;
            default:
                layoutParticipantCircle(recycler, -90, getVerticalSpace()/2 - decoratedChildHeight);
                break;
        }
    }

    private Point produceCirclePoint(Point center, double angle, int currentRadius) {
        Point point = new Point();
        float radian = (float) Math.toRadians(angle);
        int x = center.x + (int) (Math.cos(radian) * currentRadius);
        int y = center.y + (int) (Math.sin(radian) * currentRadius);
        point.set(x,y);
        return point;
    }

    private Rect produceLayoutRect(Point center, int rectWidth, int rectHeight) {
        Rect rect = new Rect(
                center.x - rectWidth/2,
                center.y - rectHeight/2,
                center.x + rectWidth/2,
                center.y + rectHeight/2
        );
        return rect;
    }

    private void layoutParticipantCircle(RecyclerView.Recycler recycler, double initialAngle, int radius) {
        View view = getChildAt(0);
        Point point = produceCirclePoint(center, initialAngle, radius);
        Rect rect = produceLayoutRect(point, decoratedChildWidth, decoratedChildHeight);
        layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        double angle = initialAngle;
        double intervalAngle = 360 / getItemCount();
        for(int i=1; i<getItemCount(); i++) {
            angle += intervalAngle;

            view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);

            point = produceCirclePoint(center, angle, radius);
            rect = produceLayoutRect(point, decoratedChildWidth, decoratedChildHeight);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingStart() - getPaddingEnd();
    }

}
