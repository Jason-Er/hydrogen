package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.LineTypeEvent;
import com.thumbstage.hydrogen.model.bo.LineType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTypeAdapter extends RecyclerView.Adapter{

    List<LineType> items = new ArrayList(){
        {
            add(LineType.LT_DIRECTION);
            add(LineType.LT_DIALOGUE);
        }};

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_line_type)
        TextView textView;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new LineTypeEvent(v, "click"));
                }
            });
        }

        void setLineType(LineType lineType) {
            // textView.setText(lineType.name());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview_linetype, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).setLineType(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
