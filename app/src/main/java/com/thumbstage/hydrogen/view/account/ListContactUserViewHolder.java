package com.thumbstage.hydrogen.view.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListContactUserViewHolder extends RecyclerView.ViewHolder {

    User user;

    @BindView(R.id.item_contact_user_avatar)
    ImageView avatar;
    @BindView(R.id.item_contact_user_name)
    TextView name;

    public ListContactUserViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        name.setText(user.getName());
        if(user != null) {
            Glide.with(avatar).load(user.getAvatar()).into(avatar);
        }
    }

}
