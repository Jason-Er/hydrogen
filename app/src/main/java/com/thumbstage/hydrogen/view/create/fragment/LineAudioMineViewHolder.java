package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.create.type.LineEx;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineAudioMineViewHolder extends RecyclerView.ViewHolder {

    Line line;

    @BindView(R.id.item_line_right_tv_time)
    TextView time;
    @BindView(R.id.item_line_right_iv_avatar)
    ImageView avatar;

    public LineAudioMineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLine(LineEx line) {
        this.line = line;
        GlideUtil.inject(itemView.getContext(), line.getWho().getAvatar(), avatar);
        if(line.isNeedShowTime()) {
            time.setText(StringUtil.date2String4Show(line.getWhen()));
        } else {
            time.setVisibility(View.GONE);
        }
    }
}
