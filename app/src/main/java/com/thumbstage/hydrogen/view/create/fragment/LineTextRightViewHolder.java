package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTextRightViewHolder extends RecyclerView.ViewHolder {

    Line line;

    @BindView(R.id.item_line_right_tv_time)
    TextView time;
    @BindView(R.id.item_line_right_iv_avatar)
    ImageView avatar;
    @BindView(R.id.item_line_right_text_tv_content)
    TextView content;

    public LineTextRightViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLine(Line line) {
        this.line = line;
        GlideUtil.inject(itemView.getContext(), line.getWho().getAvatar(), avatar);
        content.setText(line.getWhat());
        time.setText(StringUtil.date2String4Show(line.getWhen()));
    }
}
