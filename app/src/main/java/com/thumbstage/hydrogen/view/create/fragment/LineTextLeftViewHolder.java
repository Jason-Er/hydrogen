package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.repository.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTextLeftViewHolder extends RecyclerView.ViewHolder {

    Line line;

    @BindView(R.id.item_line_left_tv_time)
    TextView time;
    @BindView(R.id.item_line_left_iv_avatar)
    ImageView avatar;
    @BindView(R.id.item_line_left_text_tv_content)
    TextView content;


    public LineTextLeftViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLine(@NonNull Line line) {
        this.line = line;
        LCRepository.getUser(line.getWho(), new LCRepository.IReturnUser() {
            @Override
            public void callback(User user) {
                GlideUtil.inject(itemView.getContext(), user.getAvatar(), avatar);
            }
        });
        content.setText(line.getWhat());
        time.setText(StringUtil.date2String4Show(line.getWhen()));
    }
}
