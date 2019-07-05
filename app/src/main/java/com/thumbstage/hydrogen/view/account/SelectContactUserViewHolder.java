package com.thumbstage.hydrogen.view.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.SelectContactUserEvent;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectContactUserViewHolder extends RecyclerView.ViewHolder {

    User user;

    @BindView(R.id.item_select_user_avatar)
    ImageView avatar;
    @BindView(R.id.item_select_user_name)
    TextView name;
    @BindView(R.id.item_select_user_check)
    CheckBox checkBox;

    public SelectContactUserViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EventBus.getDefault().post(new SelectContactUserEvent(user.getId(), isChecked));
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        name.setText(user.getName());
        if(user != null) {
            GlideUtil.inject(avatar.getContext(), user.getAvatar(),avatar);
        }
    }

}
