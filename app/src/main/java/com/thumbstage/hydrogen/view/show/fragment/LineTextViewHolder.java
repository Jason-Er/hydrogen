package com.thumbstage.hydrogen.view.show.fragment;

import android.view.View;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTextViewHolder extends ViewHolder {

    @BindView(R.id.item_line_left_text_tv_content)
    TextView content;

    public LineTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }
}
