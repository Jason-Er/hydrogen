package com.thumbstage.hydrogen.view.show.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTextViewHolder extends ViewHolder {

    @BindView(R.id.item_line_left_text_tv_content)
    TextView content;

    IFinishCallBack iFinishCallBack;

    public LineTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setContent(final String content) {
        this.content.setText(content);
        itemView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("LineTextViewHolder","during time: "+content.length() * 181);
                if(iFinishCallBack != null) {
                    iFinishCallBack.finish();
                }
            }
        }, content.length() * 181);
    }

    public void setiFinishCallBack(IFinishCallBack iFinishCallBack) {
        this.iFinishCallBack = iFinishCallBack;
    }
}
