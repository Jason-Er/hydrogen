package com.thumbstage.hydrogen.view.browse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class BrowseRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Topic> topics = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_browse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BrowseItemCardView) holder.itemView).setTopic(topics.get(position));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }
}
