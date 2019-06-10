package com.thumbstage.hydrogen.view.show.fragment;

import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.utils.IMAudioHelper;
import com.thumbstage.hydrogen.view.create.fragment.IMPlayButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineAudioViewHolder extends ViewHolder{

    @BindView(R.id.chat_item_audio_play_btn)
    IMPlayButton imPlayButton;

    IFinishCallBack iFinishCallBack;

    public LineAudioViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        imPlayButton.setFinishCallback(new IMAudioHelper.AudioFinishCallback() {
            @Override
            public void onFinish() {
                if(iFinishCallBack!=null) {
                    iFinishCallBack.finish();
                }
            }
        });
    }

    public void setContent(String content) {
        imPlayButton.setPath(content);
        imPlayButton.onClick(imPlayButton);
    }

    public void setiFinishCallBack(IFinishCallBack iFinishCallBack) {
        this.iFinishCallBack = iFinishCallBack;
    }
}
