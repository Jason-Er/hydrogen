package com.thumbstage.hydrogen.view.browse.published;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicInfo;

import java.util.ArrayList;
import java.util.List;

public class PublishedOpenedRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<TopicInfo> topicInfos = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_published_opened, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PublishedOpenedItemCardView) holder.itemView).setTopicInfo(topicInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return topicInfos.size();
    }

    public void setTopicInfos(List<TopicInfo> topicInfos) {
        this.topicInfos = topicInfos;
        notifyDataSetChanged();
    }
}
