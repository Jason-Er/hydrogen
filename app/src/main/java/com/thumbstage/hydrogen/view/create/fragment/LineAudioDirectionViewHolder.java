package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.create.type.LineEx;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineAudioDirectionViewHolder extends RecyclerView.ViewHolder {

    Line line;

    @BindView(R.id.item_line_center_tv_time)
    TextView time;
    @BindView(R.id.chat_item_audio_play_btn)
    IMPlayButton imPlayButton;

    public LineAudioDirectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLine(LineEx line) {
        this.line = line;
        if(line.isNeedShowTime()) {
            time.setText(StringUtil.date2String4Show(line.getWhen()));
        } else {
            time.setVisibility(View.GONE);
        }
        imPlayButton.setPath(line.getWhat());
    }
}
