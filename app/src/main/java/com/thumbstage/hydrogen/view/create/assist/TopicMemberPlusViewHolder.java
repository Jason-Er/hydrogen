package com.thumbstage.hydrogen.view.create.assist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.thumbstage.hydrogen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicMemberPlusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_member_plus)
    ImageView plus;

    public TopicMemberPlusViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
